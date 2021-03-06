package ufal.ic.so;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/** @author Adeilson Lima */
public class EscalonadorDeTarefas {
	/** quantum definito para Round-Robin */
	private static int quantumRR = 2;
	/** passado para o programa na execucao em args[0] */
	private static String nomeArquivo;
	/** passado para o programa na execucao em args[1] */
	private static Politica politica;

	/** Tempo total do processador */
	private static int tmax;

	/** Recurso processador */
	private static Processador processador;

	/** Lista de tarefas */
	private static List<Tarefa> tarefas;

	/** Lista tarefas prontas */
	private static List<Tarefa> tarefasProntas;

	private static boolean preemptivo = false;

	/** tarefa no estado executando */
	private static Tarefa tarefaRodando = null;

	public static void main(String[] args) throws IOException {
		/** Verifica se todos os parametros foram passados */
		if (args.length != 2) {
			System.out.println("Falta argumentos! Tente novamente...");
			System.out.println("Exemplo: java EscalonadorDeTarefas input.txt fcfs");
			System.exit(1);
		}
		nomeArquivo = args[0];
		politica = Politica.valueOf(args[1]);

		if (politica == Politica.rr || politica == Politica.rr2 || politica == Politica.rr3) {
			preemptivo = true;
		}

		System.out.println(nomeArquivo + " " + politica);

		/** Ler os dados da tarefa no arquivo de entrada */
		lerArquivo();

		/** Altera a saida padrao */
		System.out.println("A saida sera impressa no arquivo \"output\"");
		arquivoSaida();

		/** Ordena as tarefas pela data de criacao */
		ordenaTarefasInicio();

		/** Faz o escalonamento das tarefas segunda a politica escolhida */
		escalonaTarefas();

	}

	/** Faz o escalonamento das tarefas segunda a politica escolhida */
	private static void escalonaTarefas() {
		/** Tempo total de todas as tarefas */
		tmax = tempoTotal();
		int t = 0;

		processador = Processador.getInstancia();
		tarefasProntas = new LinkedList<Tarefa>();

		while (t < tmax) {

			if (!processador.isLivre()) {
				if (tarefaRodando.getTempoRestante() == 0) {
					tarefaRodando.setEstado(Estado.Terminda);
					tarefaRodando = null;
					processador.free();
				} else {
					if (preemptivo) {
						if (tarefaRodando.getQuantumAtual() == 0) {
							tarefasProntas.add(tarefaRodando);
							tarefaRodando.setEstado(Estado.Pronta);
							processador.free();
						}
					}
				}
			}
			for (Tarefa tarefa : tarefas) {
				/** coloca a tarefa na fila de prontas */
				if (tarefa.getCriacao() == t) {
					tarefasProntas.add(tarefa);
					tarefa.setEstado(Estado.Pronta);

				}
			}
			if (processador.isLivre()) {
				/** verifica se existe tarefas no estado pronta */
				if (tarefasProntas.size() != 0) {
					tarefaRodando = escolheTarefa();
					tarefaRodando.setEstado(Estado.Executando);
					processador.lock();
				}
			}
			if (politica == Politica.rr3) {
				for (Tarefa tarefa : tarefasProntas) {
					/**
					 * prioridade das tarefas "prontas" sao incrementadas em 1
					 */
					tarefa.incPrioridadeDinamica();
				}
			}
			/** imprime linha do diagrama com o estado de cada tarefa */
			imprimeLinha(t);

			++t;
			if (tarefaRodando != null) {
				tarefaRodando.decrementaTempoRestante();
				tarefaRodando.decrementaQuantum();
			} else {
				++tmax;
			}
		}

	}

	private static Tarefa escolheTarefa() {
		switch (politica) {
		case fcfs: {
			return tarefasProntas.remove(0);
		}
		case rr: {
			Tarefa t = tarefasProntas.remove(0);
			t.setQuantumAtual(quantumRR);
			return t;

		}
		case rr2: {
			Collections.sort(tarefasProntas);
			Tarefa t = tarefasProntas.remove(0);
			t.setQuantumAtual(quantumRR);
			return t;

		}
		case rr3: {
			Collections.sort(tarefasProntas);
			Tarefa t = tarefasProntas.remove(0);
			t.setQuantumAtual(quantumRR);
			t.resetPrioridadeDinamicia();// prioridade dinamica = estatica

			return t;

		}

		default:
			return null;
		}

	}

	/**
	 * imprime linha do diagrama com o estado de cada tarefa no tempo t
	 * 
	 * @param t
	 *            tempo atual
	 */
	private static void imprimeLinha(int t) {
		System.out.printf("%2d-%2d ", t, (t + 1));
		for (Tarefa tarefa : tarefas) {
			switch (tarefa.getEstado()) {
			case Executando: {
				System.out.printf("%4s", "##");
				break;
			}
			case Pronta: {
				System.out.printf("%4s", "--");
				break;
			}
			case Nova:
			case Terminda: {
				System.out.printf("%4s", " ");
			}

			default:
				break;
			}
		}
		System.out.println();

	}

	/** Calcula o tempo total de todas as tarefas */
	private static int tempoTotal() {
		int t = 0;
		for (Tarefa tarefa : tarefas) {
			t += tarefa.getDuracao();
		}
		return t;
	}

	/** Ler os dados da tarefa no arquivo de entrada */
	private static void lerArquivo() {
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
				/** Ler os numeros e descarta os espacos */
				StringTokenizer token = new StringTokenizer(linha, " ");
				String[] valores = new String[3];
				for (int i = 0; i < 3; ++i) {
					valores[i] = token.nextToken();
				}
				Tarefa tarefa = new Tarefa(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]),
						Integer.parseInt(valores[2]));
				if (tarefa.getCriacao() >= 0 && tarefa.getDuracao() >= 1) {
					tarefas.add(tarefa);
				} else {
					System.out.println(
							"Tarefas com tempo de criação menor que 0 e/ou duração menor 1 não entram no processamento!");
				}

			}
			return;

		} catch (FileNotFoundException e) {
			System.out.println("Arquivo \"" + nomeArquivo + "\" nao encontrado.");
			System.out.println("Coloque-o na raiz no projeto.");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Saiu!");
			System.exit(1);
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

	/** altera saida padrao para arquivo */
	private static void arquivoSaida() {
		// cria arquivo
		FileOutputStream f;
		try {
			f = new FileOutputStream("output");
			System.setOut(new PrintStream(f));
		} catch (FileNotFoundException e) {
			System.out.println("Nao foi possivel criar o arquivo \"output\"");
		}
	}

	/** Ordena as tarefas pela data de criacao */
	private static void ordenaTarefasInicio() {

		Collections.sort(tarefas, new Comparator<Tarefa>() {

			@Override
			public int compare(Tarefa o1, Tarefa o2) {
				if (o1.getCriacao() > o2.getCriacao()) {
					return 1;
				} else if (o1.getCriacao() < o2.getCriacao()) {
					return -1;
				}
				return o2.getPrioridade() - o1.getPrioridade();
			}

		});
		System.out.print("tempo ");
		for (int i = 0; i < tarefas.size(); ++i) {
			tarefas.get(i).setId("P" + (i + 1));
			System.out.printf("%4s", tarefas.get(i).getId());
		}
		System.out.println();

	}
}