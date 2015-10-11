package ufal.ic.so;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/** @author Adeilson Lima */
public class EscalonadorDeTarefas {
	/** quantum definito para Round-Robin */
	private static int quantumRR = 2;
	/** passado para o programa na execução em args[0] */
	private static String nomeArquivo;
	/** passado para o programa na execução em args[1] */
	private static Politica politica;

	/** Lista de tarefas */
	static List<Tarefa> tarefas;

	public static void main(String[] args) throws IOException {
		/**Verifica se todos os parametros foram passados*/
		if (args.length != 2) {
			System.out.println("Falta argumentos! Tente novamente...");
			System.out.println("Ex: java EscalonadorDeTarefas input.txt fcfs");
			System.exit(1);
		}
		nomeArquivo = args[0];
		politica = Politica.valueOf(args[1]);

		System.out.println(nomeArquivo + " " + politica);

		tarefas = new ArrayList<Tarefa>();

		FileInputStream stream = null;
		InputStreamReader streamReader = null;
		BufferedReader reader = null;
		try {
			stream = new FileInputStream(nomeArquivo);
			streamReader = new InputStreamReader(stream);
			reader = new BufferedReader(streamReader);

			String linha = null;
			/** ler arquivo de entrada linha por linha */
			while ((linha = reader.readLine()) != null) {
				/**Ler os números e descarta os espaços*/
				StringTokenizer token = new StringTokenizer(linha, " ");
				String [] valores = new String[3];
				for(int i=0;i<3;++i){
					valores[i]=token.nextToken();
				}
				Tarefa tarefa = new Tarefa();
				tarefa.setCriacao(Integer.parseInt(valores[0]));
				tarefa.setDuracao(Integer.parseInt(valores[1]));
				tarefa.setPrioridade(Integer.parseInt(valores[2]));
				tarefas.add(tarefa);
			}

			for (Tarefa t : tarefas) {
				System.out.println(t.getCriacao());
			}
			ordenaTarefas();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** fechando os objetos de I/O */
		finally {
			if (reader != null) {

				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (streamReader != null) {
				try {
					streamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**Ordena as tarefas pela data de criacao*/
	private static void ordenaTarefas() {
		
		Collections.sort(tarefas);
		System.out.print("tempo   ");
		for (int i=0;i <tarefas.size();++i) {
			tarefas.get(i).setId("P"+(i+1));
			System.out.print(tarefas.get(i).getId()+"   ");
		}
		
	}
}