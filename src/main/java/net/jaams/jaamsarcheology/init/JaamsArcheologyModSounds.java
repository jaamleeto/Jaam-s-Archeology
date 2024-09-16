
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.jaams.jaamsarcheology.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import net.jaams.jaamsarcheology.JaamsArcheologyMod;

public class JaamsArcheologyModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JaamsArcheologyMod.MODID);
	public static final RegistryObject<SoundEvent> AMMONITE_FIRED = REGISTRY.register("ammonite_fired", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "ammonite_fired")));
	public static final RegistryObject<SoundEvent> AMMONITE_BREAKS = REGISTRY.register("ammonite_breaks", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "ammonite_breaks")));
	public static final RegistryObject<SoundEvent> PRIMITIVE_SPEAR_FIRED = REGISTRY.register("primitive_spear_fired", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "primitive_spear_fired")));
	public static final RegistryObject<SoundEvent> PRIMITIVE_SPEAR_GROUND = REGISTRY.register("primitive_spear_ground", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "primitive_spear_ground")));
	public static final RegistryObject<SoundEvent> PRIMITIVE_SPEAR_HIT = REGISTRY.register("primitive_spear_hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "primitive_spear_hit")));
	public static final RegistryObject<SoundEvent> PRIMITIVE_SPEAR_RETURN = REGISTRY.register("primitive_spear_return", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "primitive_spear_return")));
	public static final RegistryObject<SoundEvent> SPEAR_FRAGMENT_FIRED = REGISTRY.register("spear_fragment_fired", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_fragment_fired")));
	public static final RegistryObject<SoundEvent> SPEAR_FRAGMENT_GROUND = REGISTRY.register("spear_fragment_ground", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_fragment_ground")));
	public static final RegistryObject<SoundEvent> SPEAR_FRAGMENT_HIT = REGISTRY.register("spear_fragment_hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_fragment_hit")));
	public static final RegistryObject<SoundEvent> SPEAR_OF_LONGINUS_GROUND = REGISTRY.register("spear_of_longinus_ground", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_of_longinus_ground")));
	public static final RegistryObject<SoundEvent> SPEAR_OF_LONGINUS_HIT = REGISTRY.register("spear_of_longinus_hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_of_longinus_hit")));
	public static final RegistryObject<SoundEvent> SPEAR_OF_LONGINUS_RETURN = REGISTRY.register("spear_of_longinus_return", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_of_longinus_return")));
	public static final RegistryObject<SoundEvent> SPEAR_OF_LONGINUS_THROW = REGISTRY.register("spear_of_longinus_throw", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "spear_of_longinus_throw")));
	public static final RegistryObject<SoundEvent> IDOL_BREAKS = REGISTRY.register("idol_breaks", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "idol_breaks")));
	public static final RegistryObject<SoundEvent> WAR_ROAR_AXE_CRITICAL = REGISTRY.register("war_roar_axe_critical", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "war_roar_axe_critical")));
	public static final RegistryObject<SoundEvent> EXPLOSIVE_IDOL_STARTED = REGISTRY.register("explosive_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "explosive_idol_started")));
	public static final RegistryObject<SoundEvent> EXPLOSIVE_IDOL_ENDS = REGISTRY.register("explosive_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "explosive_idol_ends")));
	public static final RegistryObject<SoundEvent> PIGMAN_IDOL_STARTED = REGISTRY.register("pigman_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "pigman_idol_started")));
	public static final RegistryObject<SoundEvent> PIGMAN_IDOL_ENDS = REGISTRY.register("pigman_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "pigman_idol_ends")));
	public static final RegistryObject<SoundEvent> ROTTEN_IDOL_STARTED = REGISTRY.register("rotten_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "rotten_idol_started")));
	public static final RegistryObject<SoundEvent> ROTTEN_IDOL_ENDS = REGISTRY.register("rotten_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "rotten_idol_ends")));
	public static final RegistryObject<SoundEvent> SKELETAL_IDOL_STARTED = REGISTRY.register("skeletal_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "skeletal_idol_started")));
	public static final RegistryObject<SoundEvent> SKELETAL_IDOL_ENDS = REGISTRY.register("skeletal_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "skeletal_idol_ends")));
	public static final RegistryObject<SoundEvent> STRANGE_IDOL_STARTED = REGISTRY.register("strange_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "strange_idol_started")));
	public static final RegistryObject<SoundEvent> STRANGE_IDOL_ENDS = REGISTRY.register("strange_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "strange_idol_ends")));
	public static final RegistryObject<SoundEvent> TRAITOR_IDOL_STARTED = REGISTRY.register("traitor_idol_started", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "traitor_idol_started")));
	public static final RegistryObject<SoundEvent> TRAITOR_IDOL_ENDS = REGISTRY.register("traitor_idol_ends", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("jaams_archeology", "traitor_idol_ends")));
}
