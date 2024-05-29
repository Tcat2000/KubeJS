package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.core.IngredientKJS;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class REIEntryWrappers {
	public static class WrapperRegistryEvent extends Event {
		private final REIEntryWrappers wrappers;

		private WrapperRegistryEvent(REIEntryWrappers wrappers) {
			this.wrappers = wrappers;
		}

		public <T, C> void add(EntryType<T> type, Function<Object, C> converter, Function<C, ? extends Predicate<T>> filter, Function<C, ? extends Iterable<T>> entries) {
			wrappers.add(type, converter, filter, entries);
		}
	}

	private final Map<EntryType<?>, EntryWrapper<?, ?>> entryWrappers;

	public REIEntryWrappers() {
		this.entryWrappers = new HashMap<>();
		add(VanillaEntryTypes.ITEM, IngredientJS::of, Function.identity(), IngredientKJS::kjs$getDisplayStacks);
		add(VanillaEntryTypes.FLUID, FluidWrapper::wrapArch, fs -> fs::isFluidEqual, List::of);
		NeoForge.EVENT_BUS.post(new WrapperRegistryEvent(this));
	}

	public <T, C> void add(EntryType<T> type, Function<Object, C> converter, Function<C, ? extends Predicate<T>> filter, Function<C, ? extends Iterable<T>> entries) {
		this.entryWrappers.put(type, new EntryWrapper<>(type, converter, filter, entries));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> EntryWrapper<T, ?> getWrapper(EntryType<T> type) {
		var wrapper = entryWrappers.get(type);

		if (wrapper == null) {
			wrapper = new EntryWrapper<>(type, Function.identity(), c -> c::equals, c -> (List<T>) List.of(c));
			entryWrappers.put(type, wrapper);
		}

		return (EntryWrapper) wrapper;
	}

	public Collection<EntryWrapper<?, ?>> getWrappers() {
		return entryWrappers.values();
	}
}
