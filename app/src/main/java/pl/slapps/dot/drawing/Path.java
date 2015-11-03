package pl.slapps.dot.drawing;

import android.graphics.Color;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

import pl.slapps.dot.route.Route;
import pl.slapps.dot.route.RouteBackground;

/**
 * Created by piotr on 01.11.15.
 */
public class Path {

    /*

    TO DO

    ArrayList of Verticles. Building float and short array based on arraylist.
    Connecting quads by checking duplicated verticles. If match connect verticles into one quad.
    If not create new quad and populate indices list.

     */
    public float[] roadVert;
    public short[] roadIndices;

    public FloatBuffer roadBufferedVertex;
    public ShortBuffer roadBufferedIndices;

    private String TAG = Path.class.getName();

    public float[] verticles;
    public short[] indices;

    public float r = 0.0f;
    public float g = 0.0f;
    public float b = 0.0f;
    public float a = 0.0f;

    //private ArrayList<Verticle> verticlesArray;
    ArrayList<Quad> quads; //list of quads ordered from beginning to end

    public Path(ArrayList<Route> routes, String color)
    {

        ArrayList<RouteBackground> backgrounds = new ArrayList<>();
        ArrayList<Quad> data = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            backgrounds.addAll(routes.get(i).getBackgrounds());
        }

        Log.d(TAG, " start "+routes.get(0).getType());
        for(int i=0;i<backgrounds.size();i++)
        {
            data.add(backgrounds.get(i).quad);
        }


        if (color == null)
            color = "#B8B8B8";
        int intColor = Color.parseColor(color);
        a = (float) Color.alpha(intColor) / 255;
        r = (float) Color.red(intColor) / 255;
        g = (float) Color.green(intColor) / 255;
        b = (float) Color.blue(intColor) / 255;

        //verticlesArray = new ArrayList<>();
        this.quads=new ArrayList<>();

        verticles=data.get(0).vertices;
        indices=data.get(0).indices;
        Quad result = data.get(0);


        for(int i=0;i<data.size();i++)
        {


            addQuad(data.get(i));
            //result=connectQuads(result,quads.get(i), Route.Direction.TOP);
        }

        Log.d(TAG,"quads size "+this.quads.size());


        verticles = new float[this.quads.size()* 2 *4];
        indices = new short[this.quads.size()*2*3];

        //Building arrays

        for(int j=0;j<this.quads.size();j++)
        {
            int startVerticlesIndex= j*8;
            int startIncentIndex = j*6;

            Quad q = quads.get(j);
            Log.d(TAG,"strt index "+startVerticlesIndex +" "+startIncentIndex);
            verticles[startVerticlesIndex++]=q.bottomLeft.x;
            verticles[startVerticlesIndex++]=q.bottomLeft.y;
            //verticles[startVerticlesIndex++]=q.bottomLeft.z;

            verticles[startVerticlesIndex++]=q.topLeft.x;
            verticles[startVerticlesIndex++]=q.topLeft.y;
            //verticles[startVerticlesIndex++]=q.topLeft.z;

            verticles[startVerticlesIndex++]=q.bottomRight.x;
            verticles[startVerticlesIndex++]=q.bottomRight.y;
            //verticles[startVerticlesIndex++]=q.bottomRight.z;

            verticles[startVerticlesIndex++]=q.topRight.x;
            verticles[startVerticlesIndex++]=q.topRight.y;
            //verticles[startVerticlesIndex++]=q.topRight.z;

            indices[startIncentIndex++]=q.bottomLeft.index;
            indices[startIncentIndex++]=q.topLeft.index;
            indices[startIncentIndex++]=q.bottomRight.index;

            indices[startIncentIndex++]=q.bottomRight.index;
            indices[startIncentIndex++]=q.topLeft.index;
            indices[startIncentIndex++]=q.topRight.index;



        }
        Log.d(TAG,"result "+ Arrays.toString(verticles) +"  "+Arrays.toString(indices));


        roadVert=verticles;
        roadIndices=indices;
        ByteBuffer bytes = ByteBuffer.allocateDirect(roadVert.length * 4);
        bytes.order(ByteOrder.nativeOrder());

        roadBufferedVertex = bytes.asFloatBuffer();
        roadBufferedVertex.put(roadVert);
        roadBufferedVertex.position(0);

        bytes = ByteBuffer.allocateDirect(roadIndices.length * 2);
        bytes.order(ByteOrder.nativeOrder());

        roadBufferedIndices= bytes.asShortBuffer();
        roadBufferedIndices.put(roadIndices);
        roadBufferedIndices.position(0);
        //verticles=result.vertices;
        //indices=result.indices;


    }

    public void draw(GL10 gl)
    {
        gl.glLoadIdentity();
        gl.glColor4f(r, g, b, a);


        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, roadBufferedVertex);
        gl.glDrawElements(GL10.GL_TRIANGLES, roadIndices.length,
                GL10.GL_UNSIGNED_SHORT,roadBufferedIndices);
    }


    private void addQuad(Quad q)
    {
        for(int i=0;i<quads.size();i++)
        {
            Quad e = quads.get(i);
            if(q.bottomLeft.equals(e.topLeft) && q.bottomRight.equals(e.topRight))
            {
                q.topLeft.index=e.topLeft.index;
                q.topRight.index=e.topRight.index;
                e.topLeft=q.topLeft;
                e.topRight=q.topRight;
                Log.d(TAG,"quad connected top");
                return;
            }

            if(q.bottomLeft.equals(e.bottomRight) && q.topLeft.equals(e.topRight))
            {
                q.bottomRight.index=e.bottomRight.index;
                q.topRight.index=e.topRight.index;
                e.topRight=q.topRight;
                e.bottomRight=q.bottomRight;
                Log.d(TAG,"quad connected right ");
                return;
            }

            if(q.bottomRight.equals(e.bottomLeft) && q.topRight.equals(e.topLeft))
            {
                q.bottomLeft.index=e.bottomLeft.index;
                q.topLeft.index=e.topLeft.index;
                e.topLeft=q.topLeft;
                e.bottomLeft=q.bottomLeft;
                Log.d(TAG,"quad connected left ");
                return;
            }

            if(q.topLeft.equals(e.bottomLeft) && q.topRight.equals(e.bottomRight))
            {
                q.bottomRight.index=e.bottomRight.index;
                q.bottomLeft.index=e.bottomLeft.index;
                e.bottomRight=q.bottomRight;
                e.bottomLeft=q.bottomLeft;
                Log.d(TAG,"quad connected bottom ");
                return;
            }

        }


        int startIndex = quads.size()*4;
        Log.d(TAG,"quad added ");
        q.bottomRight.index+=startIndex;
        q.bottomLeft.index+=startIndex;
        q.topRight.index+=startIndex;
        q.topLeft.index+=startIndex;

        quads.add(q);

    }




}
