package com.example.bah.graphes.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bah.graphes.R;
import com.example.bah.graphes.model.Graph;
import com.example.bah.graphes.objectsDrawable.DrawableGraph;
import com.example.bah.graphes.model.arc.ModelArc;
import com.example.bah.graphes.model.node.ModelNode;

import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private Graph graph;
    private ImageView imgView;
    private DrawableGraph drawGraph;
    private Context context = this;

    //Enregistre les noeud de départ et d'arrivé provisoir d'un futur arc
    private ModelNode nodeDepart;
    private ModelNode nodeArrive;

    //Permet d'enregistrer les coordonnées provisoir lors de la création d'un arc ou bien d'un noeud
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    //True si l'utilisateur à cliqué sur un noeud, false sinon
    private boolean findNode;
    //if modeEdition == 1, node creation mode
    //if modeEdition == 2, arc creation mode
    //if modeEdition == 3, modification mode
    private int modeEdition;
    private boolean isLongClic;
    private ModelNode currentNode;

    //Objet Path permettant l'affichage d'un arc suivant les mouvements de l'utilisateur lors de leurs créations
    private Path currentPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        graph = new Graph();
        this.intiGraph();
        this.drawGraph = new DrawableGraph(this.graph);
        imgView = (ImageView) findViewById(R.id.graphImg);
        imgView.setImageDrawable(drawGraph);
        setMode(1);
        currentPath = new Path();
        drawGraph.setCurrentPath(currentPath);


        /****************/
        //Touch Listener
        /***************/

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x1 = event.getX();
                y1 = event.getY();
                switch (event.getAction())
                {
                    //action listener pour les simple click

                    case MotionEvent.ACTION_DOWN:
                        findNode = false;
                        for(ModelNode node : graph.getListNode()){
                            if(node.getRect().contains(event.getX(),event.getY())) {
                                currentNode = node;
                                //Debut de création d'un arc
                                if(modeEdition == 2){
                                    nodeDepart = currentNode;
                                    currentPath.moveTo(nodeDepart.getX(), nodeDepart.getY());

                                }
                                findNode = true;

                            }
                        }
                        //permet de mettre à jour la vue
                        imgView.invalidate();



                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = event.getX();
                        y2 = event.getY();
                        //Deplacement de l'arc provisoir
                        if(findNode && getMode()==2){
                            currentPath = new Path();
                            currentPath.moveTo(nodeDepart.getX(), nodeDepart.getY());
                            currentPath.quadTo(x2, y2,x2, y2);
                            //affichage de l'arc provisoir dans le canvas
                            drawGraph.setCurrentPath(currentPath);
                        }
                        //Déplacement d'un noeud
                        else if(findNode && getMode()==3){
                            currentNode.setCoord(x2, y2);
                            //On update les arcs ayant pour noeud de depart ou arrive le noeud deplacé
                            Iterator<ModelArc> it = graph.getArcForNode(currentNode).iterator();
                            while(it.hasNext()){
                                it.next().updatePath();
                            }
                        }
                        imgView.invalidate();
                        break;

                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();

                        if(findNode && !isLongClic){
                            for(ModelNode node : graph.getListNode()) {
                                if (node.getRect().contains(event.getX(), event.getY())) {
                                    //Cree l'arc
                                    if( getMode() == 2){
                                        nodeArrive = node;


                                        //Définition du nom de l'arc à l'aide d'une fenetre de dialogue
                                        final Dialog dialog = new Dialog(context);
                                        dialog.setContentView(R.layout.dialog_newarc);
                                        dialog.setTitle("nom du nouvel arc");

                                        final EditText textNewArc = (EditText) dialog.findViewById(R.id.textnewArc);

                                        Button buttonNewArc = (Button) dialog.findViewById(R.id.buttonNewArc);
                                        buttonNewArc.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ModelArc newArc = new ModelArc(textNewArc.getText().toString());
                                                newArc.setNodeDepart(nodeDepart);
                                                newArc.setNodeArriver(nodeArrive);
                                                newArc.updatePath();
                                                graph.addArc(newArc);
                                                dialog.dismiss();
                                            }
                                        });

                                        dialog.show();
                                        currentNode = null;
                                    }
                                }
                            }

                        }
                        //On reinitialise les variables
                        currentPath = new Path();
                        drawGraph.setCurrentPath(currentPath);
                        isLongClic = false;
                        findNode = false;
                        imgView.invalidate();
                        break;

                }
                return false;
            }

        });

        //Gestion des longClicks
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            isLongClic = true;
                //Creation d'un nouveau noeud
                if(modeEdition == 1 && currentNode == null){

                    //Fenetre de Dialogue pour le nom du noeud
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_newnode);
                    dialog.setTitle("nom du nouveau noeud");

                    final EditText textNewNode = (EditText) dialog.findViewById(R.id.textnewNode);

                    Button buttonNewNode = (Button) dialog.findViewById(R.id.buttonNewNode);
                    buttonNewNode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ModelNode newNode = new ModelNode(x1, y1, textNewNode.getText().toString() );
                            graph.addNode(newNode);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

                //Modification d'un noeud
                if(modeEdition == 1 && currentNode != null){
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_node);
                    dialog.setTitle("modification du noeud");

                    final EditText textNewNode = (EditText) dialog.findViewById(R.id.textnewNode);
                    Button buttonModif = (Button) dialog.findViewById(R.id.buttonModif);
                    buttonModif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //On ne modifit pas le nom du noeud avec un String vide
                            if(!textNewNode.getText().toString().equals("")){
                                currentNode.setName(textNewNode.getText().toString());
                            }
                            currentNode = null;
                            imgView.invalidate();
                            dialog.dismiss();

                        }
                    });

                    Button buttonSuppr = (Button) dialog.findViewById(R.id.buttonSuppr);
                    buttonSuppr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            graph.removeNode(currentNode);
                            Iterator<ModelArc> it = graph.getArcForNode(currentNode).iterator();
                            while(it.hasNext()){
                                graph.removeArc(it.next());
                            }
                            currentNode = null;
                            imgView.invalidate();
                            dialog.dismiss();

                        }
                    });

                    //Choix de la couleur
                    Button buttonColor = (Button) dialog.findViewById(R.id.buttonColor);
                    buttonColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogcolor = new Dialog(context);
                            dialogcolor.setContentView(R.layout.dialog_color);
                            dialogcolor.setTitle("modification de la couleur");
                            Button buttonNewColor = (Button) dialogcolor.findViewById(R.id.buttonNewColor);
                            buttonNewColor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    RadioGroup radioGroup = (RadioGroup) dialogcolor.findViewById(R.id.radioColor);
                                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                                    String selectedtext = (String) radioButton.getText();
                                    if(selectedtext.equals("Rouge")){ currentNode.setColor(Color.RED);}
                                    else if(selectedtext.equals("Vert")){ currentNode.setColor(Color.GREEN);}
                                    else if(selectedtext.equals("Bleu")){ currentNode.setColor(Color.BLUE);}
                                    else if(selectedtext.equals("Orange")){ currentNode.setColor(Color.rgb(255, 165, 0));}
                                    else if(selectedtext.equals("Cyan")){ currentNode.setColor(Color.rgb(0, 255, 255));}
                                    else if(selectedtext.equals("Magenta")){ currentNode.setColor(Color.MAGENTA);}
                                    else if(selectedtext.equals("Noir")){ currentNode.setColor(Color.BLACK);}
                                    dialogcolor.dismiss();
                                }
                            });

                            dialogcolor.show();

                        }
                    });

                    dialog.show();
                }
                return true;

            }
        });
    }

    /**
     * Permet de creer et afficher le menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Permet d'effectuer le bonne action en fonction de l'onglet selectionne dans le menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.node:
                setMode(1);
                return true;
            case R.id.arc:
                setMode(2);
                return true;
            case R.id.modif:
                setMode(3);
                return true;
            case R.id.mail:
                sendByMail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Change de mode
     * @param mode, numéro du mode
     */
    public void setMode(int mode){
        this.modeEdition = mode;
    }

    /**
     * Rend le mode courant de l'application
     */
    public int getMode(){
        return this.modeEdition ;
    }


    /**
     * Créé les 9 premiers noeuds lors de l'ouverture de l'application
     */
    public void intiGraph()
    {
        ModelNode modelRectangle1 = new ModelNode(125, 125, "node1");
        ModelNode modelRectangle2 = new ModelNode(125, 325, "node2");
        ModelNode modelRectangle3 = new ModelNode(125, 525, "node3");
        ModelNode modelRectangle4 = new ModelNode(325, 125, "node4");
        ModelNode modelRectangle5 = new ModelNode(325, 325, "node5");
        ModelNode modelRectangle6 = new ModelNode(325, 525, "node6");
        ModelNode modelRectangle7 = new ModelNode(525, 125, "node7");
        ModelNode modelRectangle8 = new ModelNode(525, 325, "node8");
        ModelNode modelRectangle9 = new ModelNode(525, 525, "node9");
        this.graph.addNode(modelRectangle1);
        this.graph.addNode(modelRectangle2);
        this.graph.addNode(modelRectangle3);
        this.graph.addNode(modelRectangle4);
        this.graph.addNode(modelRectangle5);
        this.graph.addNode(modelRectangle6);
        this.graph.addNode(modelRectangle7);
        this.graph.addNode(modelRectangle8);
        this.graph.addNode(modelRectangle9);

    }

    public void sendByMail(){
        imgView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(imgView.getDrawingCache());
        try {
            // Store image in Devise database to send image to mail
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
            Uri screenshotUri = Uri.parse(path);
            final Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
            emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent1.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            emailIntent1.setType("image/png");
            startActivity(Intent.createChooser(emailIntent1, "Send email using"));
        }
        catch(Exception e) { }
    }


}
