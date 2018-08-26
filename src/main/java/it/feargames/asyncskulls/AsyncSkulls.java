package it.feargames.asyncskulls;

import me.yamakaja.runtimetransformer.RuntimeTransformer;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsyncSkulls extends JavaPlugin {

    @Override
    public void onLoad() {
        new RuntimeTransformer(CraftMetaSkullTransformer.class);
    }

}
