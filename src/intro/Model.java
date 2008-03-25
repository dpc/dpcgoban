import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

/**
 * Helper class for RotataingStones.
 *
 * Comments left as they were.
 */
public class Model {
	/** Atrybuty wierzcho³ków - wspó³rzedne, kolor, normalne */
	VertexBuffer _cubeVertexData;

	/** Tablica trójk¹tów */
	TriangleStripArray _cubeTriangles;

	/** Kolory wierzcho³ków (R, G, B). */
	final byte[] VERTEX_COLORS = {
		0, (byte) 255, 0,             0, (byte) 255, (byte) 255,
		(byte) 255, 0, 0,             (byte) 255, 0, (byte) 255,
		(byte) 255, (byte) 255, 0,    (byte) 255, (byte) 255, (byte) 255,
		0, 0, (byte) 128,             0, 0, (byte) 255,
		0, (byte) 255, 0,             0, (byte) 255, (byte) 255,
		(byte) 255, 0, 0,             (byte) 255, 0, (byte) 255,
		(byte) 255, (byte) 255, 0,    (byte) 255, (byte) 255, (byte) 255,
		0, 0, (byte) 128,             0, 0, (byte) 255,
		0, (byte) 255, 0,             0, (byte) 255, (byte) 255,
		(byte) 255, 0, 0,             (byte) 255, 0, (byte) 255,
		(byte) 255, (byte) 255, 0,    (byte) 255, (byte) 255, (byte) 255,
		0, 0, (byte) 128,             0, 0, (byte) 255,

	};

	/** Wspó³rzêdne wierzcho³ków (x, y, z). */
	final byte[] VERTEX_POSITIONS = {
		-1, -1,  1,    1, -1,  1,   -1,  1,  1,    1,  1,  1, // front
		1, -1, -1,   -1, -1, -1,    1,  1, -1,   -1,  1, -1, // back
		1, -1,  1,    1, -1, -1,    1,  1,  1,    1,  1, -1, // right
		-1, -1, -1,   -1, -1,  1,   -1,  1, -1,   -1,  1,  1, // left
		-1,  1,  1,    1,  1,  1,   -1,  1, -1,    1,  1, -1, // top
		-1, -1, -1,    1, -1, -1,   -1, -1,  1,    1, -1,  1  // bottom
	};

	/** Normalne wierzcho³ków. */
	final byte[] VERTEX_NORMALS = {
		0, 0,  127,   0, 0,  127,   0, 0,  127,   0, 0,  127, // front
		0, 0, -128,   0, 0, -128,   0, 0, -128,   0, 0, -128, // back
		127, 0, 0,    127, 0, 0,    127, 0, 0,    127, 0, 0, // right
		-128, 0, 0,   -128, 0, 0,   -128, 0, 0,   -128, 0, 0, // left
		0,  127, 0,   0,  127, 0,   0,  127, 0,   0,  127, 0, // top
		0, -128, 0,   0, -128, 0,   0, -128, 0,   0, -128, 0, // bottom
	};

	/** Indeksy definiuj¹ce sposób po³¹czenia wierzcho³ków w trójk¹ty */
	final int[] TRIANGLE_INDICES = {
		0,  1,  2,  3,   // front
		4,  5,  6,  7,   // back
		8,  9, 10, 11,   // right
		12, 13, 14, 15,   // left
		16, 17, 18, 19,   // top
		20, 21, 22, 23,   // bottom
	};

	/** Lengths of triangle strips in TRIANGLE_INDICES. */
	int[] TRIANGLE_LENGTHS = {
		4, 4, 4, 4, 4, 4
	};

	/**
	 * Konstruktor - budowanie siatki trójk¹tów
	 */
	public Model(){
		_cubeVertexData = new VertexBuffer();

		//Dodane wspó³rzêdnych wierzcho³ków
		VertexArray vertexPositions = new VertexArray(VERTEX_POSITIONS.length/3, 3, 1);
		vertexPositions.set(0, VERTEX_POSITIONS.length/3, VERTEX_POSITIONS);
		_cubeVertexData.setPositions(vertexPositions, 1.0f, null);

		//Dodanie kolorów
		VertexArray vertexColors = new VertexArray(VERTEX_COLORS.length/3, 3, 1);
		vertexColors.set(0, VERTEX_COLORS.length/3, VERTEX_COLORS);
		_cubeVertexData.setColors(vertexColors);

		//Dodanie normalnych
		VertexArray vertexNormals = new VertexArray(VERTEX_NORMALS.length/3, 3, 1);
		vertexNormals.set(0, VERTEX_NORMALS.length/3, VERTEX_NORMALS);
		_cubeVertexData.setNormals(vertexNormals);

		//	Budowanie siatki trójk¹tów, indeksy wskazuj¹ na wspó³rzêdne
		//wierzcho³ków zdefinowanych w VERTEX_POSITIONS
		_cubeTriangles = new TriangleStripArray(TRIANGLE_INDICES, TRIANGLE_LENGTHS);
	};
}
