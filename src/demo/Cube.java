import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;

public class Cube implements Demo {

	private Graphics3D      iG3D;
	private Camera          iCamera;
	private Light           iLight;
	private float           iAngle = 0.0f;
	private Transform       iTransform = new Transform();
	private Background      iBackground = new Background();
	private VertexBuffer    iVb;    // positions, normals, colors, texcoords
	private IndexBuffer     iIb;    // indices to iVB, forming triangle strips
	private Appearance      iAppearance; // material, texture, compositing, ...
	private Material        iMaterial = new Material();
	private Image           iImage;

	/**
	 * Construct the Displayable.
	 */
	public Cube(Canvas c) throws Exception {
		init(c);
	}

	/**
	 * Component initialization.
	 */
	private void init(Canvas c) throws Exception  {
		// get the singleton Graphics3D instance
		iG3D = Graphics3D.getInstance();

		// create a camera
		iCamera = new Camera();
		iCamera.setPerspective( 60.0f,              // field of view
			(float)c.getWidth()/ (float)c.getHeight(),  // aspectRatio
			1.0f,      // near clipping plane
			1000.0f ); // far clipping plane

		// create a light
		iLight = new Light();
		iLight.setColor(0xffffff);         // white light
		iLight.setIntensity(1.25f);          // overbright

		// init some arrays for our object (cube)

		// Each line in this array declaration represents a triangle strip for
		// one side of a cube. The only primitive we can draw with is the
		// triangle strip so if we want to make a cube with hard edges we
		// need to construct one triangle strip per face of the cube.
		// 1 * * * * * 0
		//   * *     *
		//   *   *   *
		//   *     * *
		// 3 * * * * * 2
		// The ascii diagram above represents the vertices in the first line
		// (the first tri-strip)
		short[] vert = {
			10, 10, 10,  -10, 10, 10,   10,-10, 10,  -10,-10, 10,   // front
			-10, 10,-10,   10, 10,-10,  -10,-10,-10,   10,-10,-10,   // back
			-10, 10, 10,  -10, 10,-10,  -10,-10, 10,  -10,-10,-10,   // left
			10, 10,-10,   10, 10, 10,   10,-10,-10,   10,-10, 10,   // right
			10, 10,-10,  -10, 10,-10,   10, 10, 10,  -10, 10, 10,   // top
			10,-10, 10,  -10,-10, 10,   10,-10,-10,  -10,-10,-10 }; // bottom

		// create a VertexArray to hold the vertices for the object
		VertexArray vertArray = new VertexArray(vert.length / 3, 3, 2);
		vertArray.set(0, vert.length/3, vert);

		// The per-vertex normals for the cube; these match with the vertices
		// above. Each normal is perpendicular to the surface of the object at
		// the corresponding vertex.
		byte[] norm = {  
			0, 0, 127,    0, 0, 127,    0, 0, 127,    0, 0, 127,
			0, 0,-127,    0, 0,-127,    0, 0,-127,    0, 0,-127,
			-127, 0, 0,   -127, 0, 0,   -127, 0, 0,   -127, 0, 0,
			127, 0, 0,    127, 0, 0,    127, 0, 0,    127, 0, 0,
			0, 127, 0,    0, 127, 0,    0, 127, 0,    0, 127, 0,
			0,-127, 0,    0,-127, 0,    0,-127, 0,    0,-127, 0 };

		// create a vertex array for the normals of the object
		VertexArray normArray = new VertexArray(norm.length / 3, 3, 1);
		normArray.set(0, norm.length/3, norm);

		// per vertex texture coordinates
		short[] tex = {  
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1 };

		// create a vertex array for the texture coordinates of the object
		VertexArray texArray = new VertexArray(tex.length / 2, 2, 2);
		texArray.set(0, tex.length/2, tex);

		// the length of each triangle strip        
		int[] stripLen = { 4, 4, 4, 4, 4, 4 };

		// create the VertexBuffer for our object
		VertexBuffer vb = iVb = new VertexBuffer();
		vb.setPositions(vertArray, 1.0f, null);      // unit scale, zero bias
		vb.setNormals(normArray);
		vb.setTexCoords(0, texArray, 1.0f, null);    // unit scale, zero bias

		// create the index buffer for our object (this tells how to
		// create triangle strips from the contents of the vertex buffer).
		iIb = new TriangleStripArray( 0, stripLen );

		// load the image for the texture
		//iImage = Image.createImage( "/texture.png" );

		// create the Image2D (we need this so we can make a Texture2D)
		//Image2D image2D = new Image2D( Image2D.RGB, iImage );

		// create the Texture2D and enable mipmapping
		// texture color is to be modulated with the lit material color
		/*Texture2D texture = new Texture2D( image2D );
		texture.setFiltering(Texture2D.FILTER_NEAREST,
			Texture2D.FILTER_NEAREST);
		texture.setWrapping(Texture2D.WRAP_CLAMP,
			Texture2D.WRAP_CLAMP);
		texture.setBlending(Texture2D.FUNC_MODULATE);*/

		// create the appearance
		iAppearance = new Appearance();
		//iAppearance.setTexture(0, texture);
		iAppearance.setMaterial(iMaterial);
		iMaterial.setColor(Material.DIFFUSE, 0xFFFFFFFF);   // white
		iMaterial.setColor(Material.SPECULAR, 0xFFFFFFFF);  // white
		iMaterial.setShininess(90.0f);

		iBackground.setColor(0xf54588); // set the background color
	}

	/**
	 * Paint the scene.
	 */
	public void paint(Graphics g) {

		// Bind the Graphics of this Canvas to our Graphics3D. The
		// viewport is automatically set to cover the entire clipping
		// rectangle of the Graphics object. The parameters indicate
		// that z-buffering, dithering and true color rendering are
		// enabled, but antialiasing is disabled.

		iG3D.bindTarget(g, true,
			Graphics3D.DITHER |
			Graphics3D.TRUE_COLOR);

		// clear the color and depth buffers
		iG3D.clear(iBackground);

		// set up the camera in the desired position
		Transform transform = new Transform();
		transform.postTranslate(0.0f, 0.0f, 60.0f);
		iG3D.setCamera(iCamera, transform);

		// set up a "headlight": a directional light shining
		// from the direction of the camera
		iG3D.resetLights();
		iG3D.addLight(iLight, transform);

		// update our transform (this will give us a rotating cube)
		iAngle += 1.0f;
		iTransform.setIdentity();
		iTransform.postRotate(iAngle,       // rotate 1 degree per frame
			1.0f, 1.0f, 1.0f);  // rotate around this axis

		// Render our cube. We provide the vertex and index buffers
		// to specify the geometry; the appearance so we know what
		// material and texture to use; and the transform to tell
		// where to render the object
		iG3D.render(iVb, iIb, iAppearance, iTransform);

		// flush
		iG3D.releaseTarget();
	}
}
