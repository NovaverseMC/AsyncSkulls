package it.feargames.asyncskulls;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import me.yamakaja.runtimetransformer.annotation.CallParameters;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.TransformByName;
import net.minecraft.server.v1_12_R1.GameProfileSerializer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntitySkull;

import javax.annotation.Nullable;

@TransformByName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSkull")
public class CraftMetaSkullTransformer {

    private GameProfile profile;

    @CallParameters(
            type = CallParameters.Type.SPECIAL,
            owner = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaItem",
            name = "applyToItem",
            desc = "(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V"
    )
    private native void super_applyToItem(NBTTagCompound tag);

    @Inject(InjectionType.OVERRIDE)
    void applyToItem(NBTTagCompound tag) {
        super_applyToItem(tag);
        if (profile != null) {
            NBTTagCompound owner = new NBTTagCompound();
            GameProfileSerializer.serialize(owner, profile);
            tag.set("SkullOwner", owner);
            // Spigot start - do an async lookup of the profile.
            // Unfortunately there is not way to refresh the holding
            // inventory, so that responsibility is left to the user.
            TileEntitySkull.b(profile, new Predicate<GameProfile>() {
                @Override
                public boolean apply(@Nullable GameProfile input) {
                    NBTTagCompound owner1 = new NBTTagCompound();
                    GameProfileSerializer.serialize(owner1, input);
                    tag.set("SkullOwner", owner1);
                    return false;
                }
            }, false);
            // Spigot end
        }
    }

}
