package nx.pingwheel.common.compat;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public interface Component {

	static MutableComponent empty() {
		return (MutableComponent)TextComponent.EMPTY;
	}

	static MutableComponent translatable(String key, Object... args) {
		return new TranslatableComponent(key, args);
	}
}