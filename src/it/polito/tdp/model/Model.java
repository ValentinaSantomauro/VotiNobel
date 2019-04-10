package it.polito.tdp.model;

import java.util.*;

import it.polito.tdp.dao.EsameDAO;

public class Model {
	
	private List <Esame> esami; //esami letti dal database
	private List <Esame> best; //gestione della ricorsione 
	private double media_best;
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	/**
	 * Trova la combinazione di  corsi avente la somma dei crediti richiesta 
	 * che abbia la media max. 
	 * @param numeroCrediti
	 * @return l'elenco dei corsi ottimale oppure null se non esiste alcuna 
	 * combinazione di corsi che assomma al numero esatto di crediti.
	 */
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//avvio della ricorsione
		best = null;
		media_best=0.0;
		
		Set <Esame> parziale= new HashSet<>();
		
		cerca(parziale,0, numeroCrediti);
		
		return best;
	}
	
	private void cerca(Set <Esame> parziale, int L, int m) {
		//casi terminali
		//1)
		int crediti = sommaCrediti(parziale);
		if(crediti>m)
			return;
		//2
		if(crediti==m) {
			double media= calcolaMedia(parziale);
			
			if(media> media_best) {
				//ho trovato una media migliore, cambio la soluzione best
				best = new ArrayList <Esame> (parziale);
				media_best = media;
				//partendo da questa soluzione è inutile generare le altre perchè è impossibile trovare
				//una media migliore di quella appena trovata: o non aggiungo nulla (la media non cambia) o aggiungo qualcosa e sforo il numero di crediti
				//di conseguenza metto il return
			return;
	
			}
			else {
				// ho m crediti ma la media non è migliore
				return;
			}
		}
		
		//di sicuro crediti<m
		//per aggiungere crediti vorrei aggiungere corsi ma sono nel caso in cui potrei non avere più corsi disponibili
		//sono finiti gli esami della lista e i livelli --> esco
		if(L==esami.size())
			return; 
		
		
		
		
		//caso normale: genero sotto problemi
		//per ogni livello devo decidere: esami[L] lo devo aggiungere o no?
		//provo a non aggiungerlo
		cerca(parziale, L+1, m); /*richiamo la stessa funzione son lo stesso set parziale, senza aggiungere nulla
		ma vado al livello successivo, non ho modificato niete quindi non faccio backtracking*/
		
		//provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		parziale.remove(esami.get(L)); //backtracking
	}

	private double calcolaMedia(Set<Esame> parziale) {
		double media= 0.0;
		int crediti =0;
		for(Esame e : parziale) {
			media+= e.getVoto()*e.getCrediti();
			crediti+= e.getCrediti();
		}
		return media/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma=0;
		for (Esame e : parziale) {
			somma+=e.getCrediti();
		}
		return somma;
	}

}
