package ufal.ic.so;

/** @author Adeilson Lima */
public class Tarefa implements Comparable<Tarefa> {
	/** Identificador do processo */
	private String id;
	/** Entrada no sistema */
	private int criacao;
	/** Duração da tarefa */
	private int duracao;
	/** prioridade da tarefa. Crescente */
	private int prioridade;

	/** Prioridade dinaminca */
	private int prioridadeDinamicia;

	/** Estado da tarefa ex.:pronta, executando,encerrada */
	private Estado estado;
	/** Tempo restante para a termino da tarefa */
	private int tempoRestante;

	/** Monitor de quantum */
	private int quantumAtual;

	public Tarefa(int criacao, int duracao, int prioridade) {
		this.criacao = criacao;
		this.duracao = duracao;
		this.prioridade = prioridade;
		this.tempoRestante = duracao;
		this.estado = Estado.Nova;
		this.prioridadeDinamicia = prioridade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCriacao() {
		return criacao;
	}

	public int getDuracao() {
		return duracao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void incPrioridadeDinamica() {
		++this.prioridadeDinamicia;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getTempoRestante() {
		return tempoRestante;
	}

	public void decrementaTempoRestante() {
		--this.tempoRestante;
	}

	/**
	 * Compara duas tarefas
	 * 
	 * @param o
	 *            tarefa a ser comparada
	 * @return
	 */
	@Override
	public int compareTo(Tarefa o) {
		int cmp = this.prioridadeDinamicia > o.getPrioridadeDinamicia() ? -1
				: this.prioridadeDinamicia < o.getPrioridadeDinamicia() ? 1 : 0;
		return cmp;
	}

	/**
	 * @return the quantumAtual
	 */
	public int getQuantumAtual() {
		return quantumAtual;
	}

	/**
	 * @param quantumAtual
	 *            the quantumAtual to set
	 */
	public void setQuantumAtual(int quantumAtual) {
		this.quantumAtual = quantumAtual;
	}

	public void decrementaQuantum() {
		--quantumAtual;
	}

	public int getPrioridadeDinamicia() {
		return prioridadeDinamicia;
	}

	public void resetPrioridadeDinamicia() {
		this.prioridadeDinamicia = this.prioridade;
	}
}
