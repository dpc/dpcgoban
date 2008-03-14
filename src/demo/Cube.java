import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;


/**
 * Simple 3D Intro for my program.
 *
 * I'll probably erase it as soon as I present it.
 */
public class Cube implements Demo {

	private Graphics3D      iG3D;
	private Camera          iCamera;
	private Light           iLight;
	private float           iAngle = 0.0f;
	private Transform       iTransform = new Transform();
	private Background      iBackground = new Background();
	private VertexBuffer    iVb;
	private IndexBuffer     iIb;
	private Appearance      iAppearance[] = new Appearance[3];
	private long            stime = 0;
	private long            lasttime;
	private float           srot;
	private boolean         _done = false;

	/**
	 * Construct the Displayable.
	 */
	public Cube(Canvas c) throws Exception {
		init(c);
	}

	public boolean done() {
		return _done;
	}

	/**
	 * Component initialization.
	 */
	private void init(Canvas c) throws Exception  {
		iG3D = Graphics3D.getInstance();

		iCamera = new Camera();
		iCamera.setPerspective( 60.0f,              // field of view
			(float)c.getWidth()/ (float)c.getHeight(),  // aspectRatio
			1.0f,      // near clipping plane
			1000.0f ); // far clipping plane

		iLight = new Light();
		iLight.setColor(0xffffff);         // white light
		iLight.setIntensity(1.25f);          // overbright

		short[] vert = {
			10, 10, 10,  -10, 10, 10,   10,-10, 10,  -10,-10, 10,   // front
			-10, 10,-10,   10, 10,-10,  -10,-10,-10,   10,-10,-10,   // back
			-10, 10, 10,  -10, 10,-10,  -10,-10, 10,  -10,-10,-10,   // left
			10, 10,-10,   10, 10, 10,   10,-10,-10,   10,-10, 10,   // right
			10, 10,-10,  -10, 10,-10,   10, 10, 10,  -10, 10, 10,   // top
			10,-10, 10,  -10,-10, 10,   10,-10,-10,  -10,-10,-10 }; // bottom

		VertexArray vertArray = new VertexArray(vert.length / 3, 3, 2);
		vertArray.set(0, vert.length/3, vert);

		byte[] norm = {  
			0, 0, 127,    0, 0, 127,    0, 0, 127,    0, 0, 127,
			0, 0,-127,    0, 0,-127,    0, 0,-127,    0, 0,-127,
			-127, 0, 0,   -127, 0, 0,   -127, 0, 0,   -127, 0, 0,
			127, 0, 0,    127, 0, 0,    127, 0, 0,    127, 0, 0,
			0, 127, 0,    0, 127, 0,    0, 127, 0,    0, 127, 0,
			0,-127, 0,    0,-127, 0,    0,-127, 0,    0,-127, 0 };

		VertexArray normArray = new VertexArray(norm.length / 3, 3, 1);
		normArray.set(0, norm.length/3, norm);

		short[] tex = {  
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1,
			1, 0,       0, 0,       1, 1,       0, 1 };

		VertexArray texArray = new VertexArray(tex.length / 2, 2, 2);
		texArray.set(0, tex.length/2, tex);

		int[] stripLen = { 4, 4, 4, 4, 4, 4 };

		VertexBuffer vb = iVb = new VertexBuffer();
		vb.setPositions(vertArray, 1.0f, null);
		vb.setNormals(normArray);
		vb.setTexCoords(0, texArray, 1.0f, null);

		iIb = new TriangleStripArray( 0, stripLen );
		
		for (int i = 0; i < 3; ++i) {
			Image iImage = Image.createImage(
				"/texture" + Integer.toString((4 - i) % 3 + 1) +".png"
				);

			Image2D image2D = new Image2D( Image2D.RGB, iImage );


			Texture2D texture = new Texture2D( image2D );
			texture.setFiltering(Texture2D.FILTER_NEAREST,
				Texture2D.FILTER_NEAREST);
			texture.setWrapping(Texture2D.WRAP_CLAMP,
				Texture2D.WRAP_CLAMP);
			texture.setBlending(Texture2D.FUNC_MODULATE);

			iAppearance[i] = new Appearance();
			iAppearance[i].setTexture(0, texture);
			Material iMaterial = new Material();
			iAppearance[i].setMaterial(iMaterial);
			iMaterial.setColor(Material.DIFFUSE, 0xFFFFFFFF);   // white
			iMaterial.setColor(Material.SPECULAR, 0xFFFFFFFF);  // white
			iMaterial.setShininess(90.0f);
		}

	}

	/**
	 * Paint the scene.
	 */
	public void paint(Graphics g, long time) {
		if (stime == 0) {
			stime = time;
			lasttime = 0;
		}
		time -= stime;
		long dtime = time - lasttime;
		lasttime = time;

		if (time > 12000) {
			_done = true;
		}
		srot = time / 350.0f;

		iG3D.bindTarget(g, true,
			Graphics3D.DITHER |
			Graphics3D.TRUE_COLOR);

		// clear the color and depth buffers
		int x = 128 - (int)(Math.sin((float)time / 10000.0f) * 50);
		iBackground.setColor(
			(((x) * 256 + x) * 256) + 255
			);
		iG3D.clear(iBackground);

		// set up the camera in the desired position
		Transform transform = new Transform();
		transform.postTranslate(
			0.0f,
			0.0f,
			120.0f);
		iG3D.setCamera(iCamera, transform);

		// set up a "headlight": a directional light shining
		// from the direction of the camera
		iG3D.resetLights();
		iG3D.addLight(iLight, transform);

		iAngle = time / 8.3f;

		for (int i = 0; i < 3; ++i) {
			float trot = srot + (float)(Math.PI * 2.0f / 3.0f * (float)i);
			float y = 0.0f;
			long downtime = 6000 + (i * 2000);
			if (time > downtime) {
				y -= (time - downtime) / 10.0f;
			}

			iTransform.setIdentity();
			iTransform.postTranslate(
				(float)(Math.sin(trot)) * 40.0f,
				y,
				(float)(Math.cos(trot)) * 40.0f
				);
			iTransform.postRotate(
				iAngle * (float)(1.0f - i * 0.01f) + (float)(i * 11.31f),
				1.0f, 1.0f, 1.0f);


			// Render our cube. We provide the vertex and index buffers
			// to specify the geometry; the appearance so we know what
			// material and texture to use; and the transform to tell
			// where to render the object
			iG3D.render(iVb, iIb, iAppearance[i], iTransform);
		}

		// flush
		iG3D.releaseTarget();
	}
}
