package ufal.ic.so;
/**@author Adeilson Lima*/

/**Processador singlecore*/
public class Processador {
	private boolean livre;
	private static Processador instancia;
	
	/**Inicia o processador livre*/
	private Processador(){
		livre = true;
	}
	
	public static Processador getInstancia(){
		if(instancia == null){
			return new Processador();
		}else {
			return instancia;
		}
	}
	
	/**Bloqueia o processador*/
	public void lock(){
		this.livre = false;
	}
	
	/**Libera o processador*/
	public void free(){
		this.livre = true;
	}

	/**Verifica se o processador está livre
	 * @return the livre
	 */
	public boolean isLivre() {
		return livre;
	}

	
	

}
