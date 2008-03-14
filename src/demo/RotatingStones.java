import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;
import javax.microedition.lcdui.Canvas;
import java.io.IOException;

public class RotatingStones implements Demo {

	private Graphics3D _graphics3d;
 
	private boolean stop = false;
	private Model model;
 
	private Group transRotGroup;
	private World _world;
	private int appTime = 0;
	private Camera camera;

	public RotatingStones(Canvas c) {
		model = new Model();

		_graphics3d = Graphics3D.getInstance();
		_world = new World();

		//Utworzenie kamery
		camera = new Camera();
		float aspect = (float) c.getWidth() / (float) c.getHeight();
		camera.setPerspective(30.0f, aspect, 1.0f, 1000.0f);
		camera.setTranslation(0.0f, 0.0f, 10.0f);
		_world.addChild(camera);
		_world.setActiveCamera(camera);

		//Owietlenie
		Light light = new Light();
		light.setMode(Light.OMNI);
		light.setTranslation(0.0f, 0.0f, 3.0f);
		_world.addChild(light);

		//Stworzenie dwóch zestawów danych (tylko wspó³rzêdne i normalne)
		//dla szecianów niebieskich i czerowonych
		VertexBuffer blueCubeVertexData = new VertexBuffer();
		blueCubeVertexData.setDefaultColor(0x000000FF); // blue
		VertexBuffer redCubeVertexData = new VertexBuffer();
		redCubeVertexData.setDefaultColor(0x00FF0000);  // red

		VertexArray vertexPositions = new VertexArray(model.VERTEX_POSITIONS.length/3, 3, 1);
		vertexPositions.set(0, model.VERTEX_POSITIONS.length/3, model.VERTEX_POSITIONS);
		blueCubeVertexData.setPositions(vertexPositions, 1.0f, null);
		redCubeVertexData.setPositions(vertexPositions, 1.0f, null);

		VertexArray vertexNormals = new VertexArray(model.VERTEX_NORMALS.length/3, 3, 1);
		vertexNormals.set(0, model.VERTEX_NORMALS.length/3, model.VERTEX_NORMALS);
		blueCubeVertexData.setNormals(vertexNormals);
		redCubeVertexData.setNormals(vertexNormals);

		//Stworzenie siatek obiektów
		TriangleStripArray cubeTriangles = new TriangleStripArray(model.TRIANGLE_INDICES, model.TRIANGLE_LENGTHS);

		//Materia³.
		Material material = new Material();
		material.setVertexColorTrackingEnable(true);
		Appearance appearance = new Appearance();
		appearance.setMaterial(material);


		//Stworzenie grup obiektów
		//Wszystkie szeciany
		Group allMeshes = new Group();
		//allMeshes.setUserID(USER_ID_ALL_MESHES);
		_world.addChild(allMeshes);

		//Tylko niebieskie
		Group blueMeshes = new Group();
		//blueMeshes.setUserID(USER_ID_BLUE_MESHES);
		allMeshes.addChild(blueMeshes);

		//Tylko czerwone
		Group redMeshes = new Group();
		//redMeshes.setUserID(USER_ID_RED_MESHES);
		allMeshes.addChild(redMeshes);

		//grupa, która bêdzie animowana
		transRotGroup = redMeshes;

		//Stworzenie 8 szecianów
		for (int i=0; i<8; i++)
		{
			Mesh cubeMesh = null;

			if ((i%2) == 0)
			{
				cubeMesh = new Mesh(blueCubeVertexData, cubeTriangles, appearance);
				blueMeshes.addChild(cubeMesh);
			}
			else
			{
				cubeMesh = new Mesh(redCubeVertexData, cubeTriangles, appearance);
				redMeshes.addChild(cubeMesh);
			}

			cubeMesh.setTranslation(1.5f * (float) Math.cos(i*Math.PI/4), 1.5f * (float) 
				Math.sin(i*Math.PI/4), 0.0f);
			cubeMesh.setScale(0.4f, 0.4f, 0.4f);
		}

		/*
		//Ustawienie t³a
		Background backGnd = new Background();
		Image2D backIm = null;
		try {
			backIm = (Image2D)Loader.load("/clouds.gif")[0];
		} catch (IOException e) {
			e.printStackTrace();
		} 
		if (backIm != null) {
			backGnd.setImage(backIm);
			backGnd.setImageMode(Background.REPEAT, Background.REPEAT);
		}
		else
			backGnd.setColor(0x00bffe); // a light blue background

		_world.setBackground(backGnd);
*/
		setAnim();
	}

	/**
	 * Sekwencja ramek animacji
	 * @return
	 */ 
	private KeyframeSequence translateFrames()
	{
		KeyframeSequence ks = new KeyframeSequence(8, 3, KeyframeSequence.SPLINE);
		// Use a spline to interpolated between the points, so the	movement is curved 

		//  move clockwise in a circle;
		//  each frame is separated by 10 sequence time units
		ks.setKeyframe(0, 0, new float[] { -0.2f, 0.0f, 0.0f });
		ks.setKeyframe(1, 10, new float[] { -0.15f, -0.15f, 0.0f });
		ks.setKeyframe(2, 20, new float[] { 0.0f, -0.2f, 0.0f });
		ks.setKeyframe(3, 30, new float[] { 0.15f, -0.15f, 0.0f });
		ks.setKeyframe(4, 40, new float[] { 0.2f, 0.0f, 0.0f });
		ks.setKeyframe(5, 50, new float[] { 0.15f, 0.15f, 0.0f });
		ks.setKeyframe(6, 60, new float[] { 0.0f, 0.2f, 0.0f });
		ks.setKeyframe(7, 70, new float[] { -0.15f, 0.15f, 0.0f });

		ks.setDuration(80); // one cycle takes 80 sequence time units
		ks.setValidRange(0, 7);
		ks.setRepeatMode(KeyframeSequence.LOOP);
		return ks;
	}

	/**
	 * Ustawienie animacji
	 */
	private void setAnim()
	{
		AnimationController animController = new AnimationController();
		animController.setActiveInterval(0, 1500);

		//creation translation animation track
		KeyframeSequence transKS = translateFrames();
		AnimationTrack transTrack = new AnimationTrack(transKS, AnimationTrack.TRANSLATION);
		transTrack.setController(animController);
		//dodanie animacji do grupy szecianów czerwonych
		transRotGroup.addAnimationTrack(transTrack);
	}

	public void paint(Graphics graphics) {
		appTime++;
		camera.setTranslation(0.0f, (float)(Math.cos(appTime/10.0f)*10.0f), (float)(Math.sin(appTime/10.0f)*10.0f));
		_world.animate(appTime);
		_graphics3d.bindTarget(graphics);
		_graphics3d.render(_world);
		_graphics3d.releaseTarget();
	}

}
