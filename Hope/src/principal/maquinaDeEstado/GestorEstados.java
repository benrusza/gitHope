package principal.maquinaDeEstado;

import java.awt.Graphics;
import java.awt.Point;

import principal.maquinaDeEstado.estados.juego.GestorJuego;

public class GestorEstados {

	private EstadoJuego estados[];
	private EstadoJuego estadoActual;

	public GestorEstados() {
		iniciarEstados();
		iniciarEstadoActual();
	}

	private void iniciarEstados() {
		estados = new EstadoJuego[1];
		estados[0] = new GestorJuego(this);
	}
	public void cargarMapa(String url,Point puntoEntrada){
		estados[0]=new GestorJuego(this,url,puntoEntrada);
	}

	private void iniciarEstadoActual() {
		estadoActual = estados[0];
	}

	public void actualizar() {
		estadoActual.actualizar();
	}

	public void dibujar(final Graphics g) {
		estadoActual.dibujar(g);
	}

	public void cambiarEstadoActual(final int nuevoEstado) {
		estadoActual = estados[nuevoEstado];
	}

	public EstadoJuego obtenerEstadoActual() {
		return estadoActual;
	}
}
