package nx.pingwheel.common.helper;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static nx.pingwheel.common.ClientGlobal.Game;

public class MathUtils {
	private MathUtils() {}

	public static Vector3f worldToScreen(Vec3d worldPos, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
		var window = Game.getWindow();
		var camera = Game.gameRenderer.getCamera();
		var worldPosRel = new Vector4f(camera.getPos().negate().add(worldPos).toVector3f(), 1f);
		worldPosRel.mul(modelViewMatrix);
		worldPosRel.mul(projectionMatrix);

		var depth = worldPosRel.w;

		if (depth != 0) {
			worldPosRel.div(depth);
		}

		return new Vector3f(
			window.getScaledWidth() * (0.5f + worldPosRel.x * 0.5f),
			window.getScaledHeight() * (0.5f - worldPosRel.y * 0.5f),
			depth
		);
	}

	public static void rotateZ(MatrixStack matrixStack, float theta) {
		matrixStack.multiplyPositionMatrix(new Matrix4f().rotateZ(theta));
	}

	public static Vec2f calculateAngleRectIntersection(float angle, Vec2f leftTop, Vec2f rightBottom) {
		var direction = new Vec3d(Math.cos(angle), Math.sin(angle), 0f);
		var width = rightBottom.x - leftTop.x;
		var height = rightBottom.y - leftTop.y;
		direction = direction.multiply(new Vec3d(1f / width, 1f / height, 0f));

		var dx = Math.cos(angle);
		var dy = Math.sin(angle);
		var cx = width * 0.5f;
		var cy = height * 0.5f;

		if (Math.abs(direction.x) < Math.abs(direction.y)) {
			if (direction.y < 0) {
				// top
				var t = -cy / dy;
				var x = cx + t * dx;

				return new Vec2f((float)x + leftTop.x, leftTop.y);
			}

			// bottom
			var t = cy / dy;
			var x = cx + t * dx;

			return new Vec2f((float)x + leftTop.x, rightBottom.y);
		}

		if (direction.x < 0) {
			// left
			var t = -cx / dx;
			var y = cy + t * dy;

			return new Vec2f(leftTop.x, (float)y + leftTop.y);
		}

		// right
		var t = cx / dx;
		var y = cy + t * dy;

		return new Vec2f(rightBottom.x, (float)y + leftTop.y);
	}
}
