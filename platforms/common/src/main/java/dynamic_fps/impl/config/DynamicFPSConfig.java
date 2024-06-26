package dynamic_fps.impl.config;

import java.util.EnumMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import dynamic_fps.impl.PowerState;

public final class DynamicFPSConfig {
	private boolean enabled;
	private int idleTime; // Seconds
	private boolean detectIdleMovement;
	private boolean uncapMenuFrameRate;

	@SerializedName("states")
	private final Map<PowerState, Config> configs;

	private DynamicFPSConfig(boolean enabled, int abandonTime, boolean detectIdleMovement, boolean uncapMenuFrameRate, Map<PowerState, Config> configs) {
		this.enabled = enabled;
		this.idleTime = abandonTime;
		this.detectIdleMovement = detectIdleMovement;
		this.uncapMenuFrameRate = uncapMenuFrameRate;

		this.configs = new EnumMap<>(configs);

		for (PowerState state : PowerState.values()) {
			if (state.configurable) {
				this.configs.computeIfAbsent(state, Config::getDefault);
			}
		}
	}

	public static DynamicFPSConfig createDefault() {
		DynamicFPSConfig instance =  new DynamicFPSConfig(
			true,
			0,
			true,
			false,
			new EnumMap<>(PowerState.class)
		);

		instance.save();
		return instance;
	}

	public Config get(PowerState state) {
		if (state == PowerState.FOCUSED) {
			return Config.ACTIVE;
		} else {
			return configs.get(state);
		}
	}

	public boolean enabled() {
		return this.enabled;
	}

	public void setEnabled(boolean value) {
		this.enabled = value;
	}

	public int idleTime() {
		return this.idleTime;
	}

	public void setIdleTime(int value) {
		this.idleTime = value;
	}

	public boolean detectIdleMovement() {
		return this.detectIdleMovement;
	}

	/*
	public void setDetectIdleMovement(boolean value) {
		this.detectIdleMovement = value;
	}
	 */

	public boolean uncapMenuFrameRate() {
		return this.uncapMenuFrameRate;
	}

	public void setUncapMenuFrameRate(boolean value) {
		this.uncapMenuFrameRate = value;
	}

	private Map<PowerState, Config> configs() {
		return this.configs;
	}

	public static DynamicFPSConfig load() {
		return Serialization.load();
	}

	public void save() {
		Serialization.save(this);
	}
}
