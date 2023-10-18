package dev.latvian.mods.kubejs.generator;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.Lazy;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Supplier;

public class JsonGenerator {
	private final ConsoleJS console;
	private final Map<ResourceLocation, GeneratedData> map;

	public JsonGenerator(ConsoleJS c, Map<ResourceLocation, GeneratedData> m) {
		console = c;
		map = m;
	}

	public void add(ResourceLocation id, Supplier<byte[]> data) {
		map.put(id, new GeneratedData(id, Lazy.of(data)));
	}

	public void json(ResourceLocation id, JsonElement json) {
		add(new ResourceLocation(id.getNamespace(), id.getPath() + ".json"), () -> json.toString().getBytes(StandardCharsets.UTF_8));

		if (console.getDebugEnabled() || console == ConsoleJS.SERVER && DevProperties.get().dataPackOutput) {
			console.info("Generated " + id + ": " + json);
		}
	}
}
