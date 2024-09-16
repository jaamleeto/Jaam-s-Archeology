package net.jaams.jaamsarcheology.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelstake_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("jaams_archeology", "modelstake_model"), "main");
	public final ModelPart stake;

	public Modelstake_model(ModelPart root) {
		this.stake = root.getChild("stake");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition stake = partdefinition.addOrReplaceChild("stake", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 3.0F, -2.0F, 1.5708F, 0.0F, -1.5708F));
		PartDefinition stake_8_r1 = stake.addOrReplaceChild("stake_8_r1",
				CubeListBuilder.create().texOffs(11, 0).addBox(2.0F, -8.0F, 7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 11).addBox(1.0F, -9.0F, 7.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 4)
						.addBox(-4.0F, -10.0F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5).addBox(0.0F, -10.0F, 7.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 5)
						.addBox(-1.0F, -11.0F, 7.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 0).addBox(-2.0F, -12.0F, 7.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 11)
						.addBox(-6.0F, -13.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 6).addBox(-3.0F, -13.0F, 7.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-5.0F, -14.0F, 7.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(8.0F, 8.0F, -8.0F, 0.0F, 0.0F, -0.7854F));
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stake.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
