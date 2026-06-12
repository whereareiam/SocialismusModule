package me.whereareiam.socialismus.module.template;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.exception.ModuleLifecycleException;
import me.whereareiam.socialismus.logging.Logger;
import me.whereareiam.socialismus.module.SocialisticModule;
import me.whereareiam.socialismus.service.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Starter module implementation for Socialismus.
 * Replace the logging and TODOs with your own bootstrap logic.
 */
public final class Module extends SocialisticModule {
	private final @NotNull Path dataPath;
	private final @NotNull Scheduler scheduler;

	@Inject
	public Module(@Named("dataPath") @NotNull Path dataPath, @NotNull Scheduler scheduler) {
		this.dataPath = dataPath;
		this.scheduler = scheduler;
	}

	@Override
	public void onLoad() {
		createWorkingDirectory();

		Logger.info("Loading module %s v%s", module.getName(), module.getVersion());
		Logger.info("Data path: %s", dataPath);
		Logger.info("Working path: %s", workingPath);
	}

	@Override
	public void onEnable() {
		Logger.info("Module %s enabled", module.getName());

		// TODO register listeners, commands or scheduled jobs here.
	}

	@Override
	public void onDisable() {
		scheduler.cancelByModule(module.getName());
		Logger.info("Module %s disabled", module.getName());
	}

	@Override
	public void onUnload() {
		Logger.info("Module %s unloaded", module.getName());
	}

	private void createWorkingDirectory() {
		try {
			Files.createDirectories(workingPath);
		} catch (IOException exception) {
			throw new ModuleLifecycleException("Failed to create working directory for " + module.getName(), exception);
		}
	}
}
