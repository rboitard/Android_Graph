package com.example.bah.graphes.model;

import com.example.bah.graphes.model.arc.ModelArc;
import com.example.bah.graphes.model.node.ModelNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by romain on 16/10/2017.
 */

public class Graph implements Serializable {

    private HashMap<Integer, ModelNode> listNode;
    private HashMap<Integer, ModelArc> listArc;
    private int idNode;
    private int idArc;


    public Graph() {
        this.listNode = new HashMap<Integer, ModelNode>();
        this.listArc = new HashMap<Integer, ModelArc>();
        idNode = 0;

        idArc = 0;
    }

    public Collection<ModelNode> getListNode() { return this.listNode.values();}

    public Collection<ModelArc> getListArc() { return this.listArc.values();}

    public Iterator<ModelNode> getNodeIterator(){
        return this.listNode.values().iterator();
    }

    public Iterator<ModelArc> getArcIterator(){
        return this.listArc.values().iterator();
    }

    public void addNode(ModelNode node){
        node.setIdNode(idNode);
        this.listNode.put(node.getIdNode(), node);
        idNode++;
    }

    public void addArc(ModelArc arc){
        arc.setIdArc(idArc);
        this.listArc.put(arc.getIdArc(), arc);
        idArc++;
    }

    public ArrayList<ModelArc> getArcForNode(ModelNode node){
        ArrayList<ModelArc> res = new ArrayList<ModelArc>();
        Iterator<ModelArc> it = this.getArcIterator();
        ModelArc arc;
        while(it.hasNext()){
            arc = it.next();
            if(arc.getNodeArriver() == node || arc.getNodeDepart() == node){
                res.add(arc);
            }
        }
        return res;
    }

    public void removeNode(ModelNode node){
        this.listNode.remove(node.getIdNode());
    }

    public void removeArc(ModelArc arc){
        this.listArc.remove(arc.getIdArc());
    }

}
