
package net.jaams.jaamsarcheology;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

public class GlobalConfigManager {
	// Define la ruta global en la carpeta config
	private static final Path GLOBAL_SERVER_CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("jaams_archeology_server.toml");

	// Método para cargar la configuración del servidor desde la ruta global
	public static void loadGlobalServerConfig(ForgeConfigSpec serverConfigSpec) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(GLOBAL_SERVER_CONFIG_PATH).sync().autosave().build();
		configData.load();
		serverConfigSpec.setConfig(configData);
	}
}
