package com.example.bah.graphes.model.arc;

import android.graphics.Path;

import com.example.bah.graphes.model.node.ModelNode;

/**
 * Created by Bah on 11/10/2017.
 */

public class ModelArc {

    private ModelNode nodeDepart;
    private ModelNode nodeArriver;
    private Path path;
    private int idArc;
    private String name;


    public ModelArc(String name)
    {
        this.name = name;
        this.path = new Path();
    }

    public void setPath(Path path){
        this.path = path;
    }

    public Path getPath(){
        return path;
    }




    public ModelNode getNodeDepart() {
        return nodeDepart;
    }

    public void setNodeDepart(ModelNode nodeDepart) {
        this.nodeDepart = nodeDepart;
    }

    public ModelNode getNodeArriver() {
        return nodeArriver;
    }

    public void setNodeArriver(ModelNode nodeArriver) {
        this.nodeArriver = nodeArriver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void updatePath(){
        Path path = new Path();
        if(nodeDepart.equals(nodeArriver)){
            path.moveTo(this.nodeDepart.getX(), this.nodeDepart.getY());
            path.quadTo(this.nodeDepart.getX()+100, this.nodeDepart.getY(), this.nodeArriver.getX()+100, this.nodeArriver.getY()+100);
            path.moveTo(this.nodeDepart.getX()+100, this.nodeDepart.getY()+100);
            path.quadTo(this.nodeDepart.getX(), this.nodeDepart.getY()+100, this.nodeArriver.getX(), this.nodeArriver.getY());
        }
        else{
            path.moveTo(this.nodeDepart.getX(), this.nodeDepart.getY());
            path.quadTo(this.nodeArriver.getX(), this.nodeArriver.getY(), this.nodeArriver.getX(), this.nodeArriver.getY());
        }
        this.setPath(path);
    }

    public int getIdArc() {
        return idArc;
    }

    public void setIdArc(int idArc) {
        this.idArc = idArc;
    }

    @Override
    public String toString() {
        return "ModelArc{" +
                "nodeDepart=" + nodeDepart.toString() +
                ", nodeArriver=" + nodeArriver.toString() +
                '}';
    }

}
