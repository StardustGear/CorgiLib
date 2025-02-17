package corgitaco.corgilib.serialization.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class CodecUtil {

    public static final Codec<Block> BLOCK_CODEC = createLoggedExceptionRegistryCodec(Registry.BLOCK);

    public static final Codec<MobEffect> MOB_EFFECT = createLoggedExceptionRegistryCodec(Registry.MOB_EFFECT);
    public static final Codec<EntityType<?>> ENTITY_TYPE = createLoggedExceptionRegistryCodec(Registry.ENTITY_TYPE);

    public static final Codec<EntityType<?>> ENTITY_TYPE_CODEC = createLoggedExceptionRegistryCodec(Registry.ENTITY_TYPE);
    public static final Codec<Attribute> ATTRIBUTE_CODEC = createLoggedExceptionRegistryCodec(Registry.ATTRIBUTE);
    public static final Codec<Item> ITEM_CODEC = createLoggedExceptionRegistryCodec(Registry.ITEM);
    public static final Codec<Enchantment> ENCHANTMENT_CODEC = createLoggedExceptionRegistryCodec(Registry.ENCHANTMENT);
    public static final Codec<MobEffect> EFFECT_CODEC = createLoggedExceptionRegistryCodec(Registry.MOB_EFFECT);

    public static final Codec<ResourceKey<Biome>> BIOME_CODEC = ResourceLocation.CODEC.comapFlatMap(resourceLocation -> DataResult.success(ResourceKey.create(Registry.BIOME_REGISTRY, resourceLocation)), ResourceKey::location);

    public static final Codec<EquipmentSlot> EQUIPMENT_SLOT_CODEC = Codec.STRING.comapFlatMap(s -> {
        final EquipmentSlot equipmentSlotType = EquipmentSlot.byName(s.toLowerCase());
        if (equipmentSlotType == null) {
            throw new IllegalArgumentException(String.format("\"%s\" is not a valid equipmentSlotType. Valid equipmentSlotTypes: %s", s, Arrays.toString(Arrays.stream(EquipmentSlot.values()).map(EquipmentSlot::getName).toArray())));
        }
        return DataResult.success(equipmentSlotType);

    }, EquipmentSlot::getName);
    public static final Codec<Difficulty> DIFFICULTY_CODEC = Codec.STRING.comapFlatMap(s -> {
        final Difficulty difficulty = Difficulty.byName(s.toLowerCase());
        if (difficulty == null) {
            throw new IllegalArgumentException(String.format("\"%s\" is not a valid difficulty. Valid difficulties: %s", s, Arrays.toString(Arrays.stream(Difficulty.values()).map(Difficulty::getKey).toArray())));
        }
        return DataResult.success(difficulty);

    }, Difficulty::getKey);



    public static final Codec<ClickEvent.Action> CLICK_EVENT_ACTION_CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(ClickEvent.Action.valueOf(s));
        } catch (Exception e) {
            return DataResult.error(e.getMessage());
        }
    }, ClickEvent.Action::name);


    public static final Codec<ClickEvent> CLICK_EVENT_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                CLICK_EVENT_ACTION_CODEC.fieldOf("action").forGetter(ClickEvent::getAction),
                Codec.STRING.fieldOf("value").forGetter((ClickEvent::getValue))
        ).apply(builder, ClickEvent::new);
    });

    public static Function<String, DataResult<Integer>> validateColorHex() {
        return input -> {

            if (input.isEmpty()) {
                return DataResult.success(ChatFormatting.WHITE.getColor());
            }

            try {
                return DataResult.success((int) Long.parseLong(input.replace("#", "").replace("0x", ""), 16));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return DataResult.error(e.getMessage());
            }

        };
    }


    public static Codec<Integer> COLOR_FROM_HEX = Codec.STRING.comapFlatMap(validateColorHex(), Integer::toHexString);

    public record LazyCodec<TYPE>(Supplier<Codec<TYPE>> delegate) implements Codec<TYPE> {

        @Override
        public <T> DataResult<T> encode(TYPE input, DynamicOps<T> ops, T prefix) {
            return this.delegate().get().encode(input, ops, prefix);
        }

        @Override
        public <T> DataResult<Pair<TYPE, T>> decode(DynamicOps<T> ops, T input) {
            return this.delegate().get().decode(ops, input);
        }
    }


    public static final Codec<Integer> INTEGER_KEY_CODEC = Codec.STRING.comapFlatMap(s -> DataResult.success(Integer.valueOf(s)), Object::toString);

    public static Codec<Integer> intKeyRangeCodec(int min, int max) {
        Function<Integer, DataResult<Integer>> check = Codec.checkRange(min, max);
        return INTEGER_KEY_CODEC.flatXmap(check, check);
    }

    public static <T> Codec<T> createLoggedExceptionRegistryCodec(Registry<T> registry) {
        return ResourceLocation.CODEC.comapFlatMap(location -> {
            final Optional<T> result = registry.getOptional(location);

            if (result.isEmpty()) {
                StringBuilder registryElements = new StringBuilder();
                for (int i = 0; i < registry.entrySet().size(); i++) {
                    final T object = registry.byId(i);
                    registryElements.append(i).append(". \"").append(registry.getKey(object).toString()).append("\"\n");
                }

                return DataResult.error(String.format("\"%s\" is not a valid id in registry: %s.\nCurrent Registry Values:\n\n%s\n", location.toString(), registry.toString(), registryElements.toString()));
            }
            return DataResult.success(result.get());
        }, registry::getKey);
    }


    public static <T> Codec<WrapForSerialization<T>> wrapCodecForCollectionSerializing(Codec<T> codec) {
        return RecordCodecBuilder.create(builder ->
                builder.group(
                        codec.fieldOf("value").forGetter(tWrapForSerialization -> tWrapForSerialization.value)
                ).apply(builder, WrapForSerialization::new));
    }

    public static <T> WrapForSerialization<T> wrap(T toWrap) {
        return new WrapForSerialization<>(toWrap);
    }

    public record WrapForSerialization<T>(T value) {
    }

    public record WeirdnessPair<T>(T normal, T variant) {
        public static <T> Codec<WeirdnessPair<T>> codec(Codec<T> baseCodec) {
            return RecordCodecBuilder.create(
                    builder -> builder.group(
                            baseCodec.fieldOf("normal").forGetter(pair -> pair.normal),
                            baseCodec.fieldOf("variant").forGetter(pair -> pair.variant)
                    ).apply(builder, WeirdnessPair::new)
            );
        }
    }
}
