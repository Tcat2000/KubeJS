package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateMirrorCallbackJS extends BlockStateModifyCallbackJS {

	private final Mirror mirror;

	public BlockStateMirrorCallbackJS(BlockState state, Mirror mirror) {
		super(state);
		this.mirror = mirror;

	}

	public Direction mirror(Direction dir) {
		return mirror.mirror(dir);
	}

	@HideFromJS // I banish thee ambiguity to the depths of Rhino, never to be seen again!
	@Override
	public BlockStateModifyCallbackJS mirror(Mirror mirror) {
		throw new IllegalCallerException("Do not call this or you will get stuck in a loop!");
	}

	public Rotation getRotation(Direction dir) {
		return mirror.getRotation(dir);
	}

	public Mirror getMirror() {
		return mirror;
	}
}
