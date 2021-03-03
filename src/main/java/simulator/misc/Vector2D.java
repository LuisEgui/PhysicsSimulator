package simulator.misc;

import org.json.JSONArray;

public class Vector2D {

	double x;
	double y;

	public Vector2D() {
		x = y = 0.0;
	}

	public Vector2D(Vector2D v) {
		assert (v != null);
		x = v.x;
		y = v.y;
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double dot(Vector2D that) {
		assert (that != null);
		return x * that.x + y * that.y;
	}

	public double magnitude() {
		return Math.sqrt(dot(this));
	}

	public double distanceTo(Vector2D that) {
		assert (that != null);
		return minus(that).magnitude();
	}

	public Vector2D plus(Vector2D that) {
		assert (that != null);
		return new Vector2D(x + that.x, y + that.y);
	}

	public Vector2D minus(Vector2D that) {
		assert (that != null);
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

	public String toString() {
		return "[" + x + "," + y + "]";
	}

}
