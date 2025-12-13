package me.whereareiam.socialismus.module.template;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.logging.Logger;
import me.whereareiam.socialismus.module.SocialisticModule;

import java.nio.file.Path;

public class Module extends SocialisticModule {
	private final Path dataPath;

	@Inject
	public Module(@Named("dataPath") Path dataPath) {
		this.dataPath = dataPath;
	}

	@Override
	public void onLoad() {
		Logger.info("Module loaded!");
		Logger.info("Data path: " + dataPath);
		Logger.info("Module path: " + workingPath);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onUnload() {

	}
}
