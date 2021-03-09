package simulator.misc;

import org.json.JSONArray;
import java.util.Objects;

public class Vector2D {

	double x;
	double y;
	private int hashCode;

	public Vector2D() {
		x = y = 0.0;
	}

	public Vector2D(Vector2D v) {
		Objects.requireNonNull(v);
		x = v.x;
		y = v.y;
		hashCode = v.hashCode;
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double dot(Vector2D that) {
		Objects.requireNonNull(that);
		return x * that.x + y * that.y;
	}

	public double magnitude() {
		return Math.sqrt(dot(this));
	}

	public double distanceTo(Vector2D that) {
		Objects.requireNonNull(that);
		return minus(that).magnitude();
	}

	public Vector2D plus(Vector2D that) {
		Objects.requireNonNull(that);
		return new Vector2D(x + that.x, y + that.y);
	}

	public Vector2D minus(Vector2D that) {
		Objects.requireNonNull(that);
		return new Vector2D(x - that.x, y - that.y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector2D scale(double factor) {
		return new Vector2D(x * factor, y * factor);
	}

	public Vector2D unitVector() {
		if (magnitude() > 0.0)
			return scale(1.0 / magnitude());
		else
			return new Vector2D(this);
	}

	public JSONArray asJSONArray() {
		JSONArray a = new JSONArray();
		a.put(x);
		a.put(y);
		return a;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

	@Override
	public int hashCode() {
		int result = hashCode;
		if(result == 0) {
			result = Double.hashCode(x);
			result = 31 * result + Double.hashCode(y);
			hashCode = result;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2D other = (Vector2D) obj;
		return x == other.getX() && y == other.getY();
	}
}
