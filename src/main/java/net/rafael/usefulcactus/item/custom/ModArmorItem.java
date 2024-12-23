package net.rafael.usefulcactus.item.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.rafael.usefulcactus.item.ModArmorMaterials;

import java.util.List;
import java.util.Map;

/**
 * Custom armor item class that provides special effects when a full set is worn.
 */
public class ModArmorItem extends ArmorItem {
    // Map to store armor material and corresponding status effects
    private static final Map<RegistryEntry<ArmorMaterial>, List<StatusEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<RegistryEntry<ArmorMaterial>, List<StatusEffectInstance>>()
                    .put(ModArmorMaterials.CACTUS_SKIN_ARMOR_MATERIAL,
                            List.of(
                                    // Thorns-like effect (represents cactus spikes)
                new StatusEffectInstance(StatusEffects.STRENGTH, 400, 1, false, false),
                // Desert survival bonus
                new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 400, 0, false, false)
                                ))
                    .build();

    public ModArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            if (hasFullSuitOfArmorOn(player)) {
                evaluateArmorEffects(player);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    /**
     * Evaluates and applies armor effects if the player has the correct armor set.
     */
    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<RegistryEntry<ArmorMaterial>, List<StatusEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            RegistryEntry<ArmorMaterial> material = entry.getKey();
            List<StatusEffectInstance> effects = entry.getValue();

            if (hasCorrectArmorOn(material, player)) {
                addStatusEffectForMaterial(player, effects);
            }
        }
    }

    /**
     * Adds status effects to the player if they don't already have them.
     */
    private void addStatusEffectForMaterial(PlayerEntity player, List<StatusEffectInstance> effects) {
        boolean hasAllEffects = effects.stream()
                .allMatch(effect -> player.hasStatusEffect(effect.getEffectType()));

        if (!hasAllEffects) {
            effects.forEach(effect -> player.addStatusEffect(new StatusEffectInstance(effect)));
        }
    }

    /**
     * Checks if the player is wearing a full set of armor.
     */
    private boolean hasFullSuitOfArmorOn(PlayerEntity player) {
        return player.getInventory().armor.stream().noneMatch(ItemStack::isEmpty);
    }

    /**
     * Checks if the player is wearing the correct set of armor for the given material.
     */
    private boolean hasCorrectArmorOn(RegistryEntry<ArmorMaterial> material, PlayerEntity player) {
        return player.getInventory().armor.stream()
                .allMatch(stack -> stack.getItem() instanceof ArmorItem &&
                        ((ArmorItem) stack.getItem()).getMaterial() == material);
    }
}