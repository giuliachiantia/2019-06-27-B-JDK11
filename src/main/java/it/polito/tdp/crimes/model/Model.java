package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private List<Adiacenza>archi;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> best;
	
	public Model() {
		this.dao=new EventsDao();
	}
	
	public List<String> getCategoryId(){
		return dao.getCategotyID();
	}

	public List<String> getMese() {
		// TODO Auto-generated method stub
		return dao.getMese();
	}
	public void creaGrafo(String category, String month) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi=new ArrayList<>();
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(category, month));
		//aggiungo archi
		for(Adiacenza a: dao.getArchi(category, month)) {
			Graphs.addEdge(this.grafo, a.getVertice1(), a.getVertice2(), a.getPeso());
			archi.add(a);
			}
		Collections.sort(archi);
				
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Adiacenza> maggiorePesoMedio(){
		double pesoMed=0;
		int tot=0;
		List<Adiacenza> list= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			tot+=this.grafo.getEdgeWeight(e);
		}
		pesoMed= tot/(this.grafo.edgeSet().size());
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>pesoMed) {
				String r1= this.grafo.getEdgeSource(e);
				String r2= this.grafo.getEdgeTarget(e);
				Adiacenza a= new Adiacenza(r1,r2 ,(int) this.grafo.getEdgeWeight(e));
				list.add(a);
			}
		}
		return list;
		
	}
	public List<String> calcolaPercorso(Adiacenza a){
		this.best= new ArrayList<>();
		List<String> parziale=new ArrayList<>();
		parziale.add(a.getVertice1());
		cerca(parziale,a.getVertice2());
		
		return best;
		
	}

	private void cerca(List<String> parziale, String arrivo) {
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(parziale.size()>best.size() ) {
				this.best=new ArrayList<String>(parziale);
				return;
			}
		}
		
		for(String qi: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(qi)) {
				parziale.add(qi);
				cerca(parziale, arrivo);
				parziale.remove(parziale.size()-1);
			}
		}
	}
}
