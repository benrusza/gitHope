package principal.maquinaDeEstado.estados.juego;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import principal.Constantes;
import principal.control.GestorControles;
import principal.entes.Bloque;
import principal.entes.Jugador;
import principal.entes.Objeto;
import principal.entes.Plataforma;
import principal.entes.Puerta;
import principal.entes.enemigos.Enemigo;
import principal.mapas.Mapa;
import principal.maquinaDeEstado.EstadoJuego;
import principal.maquinaDeEstado.GestorEstados;

public class GestorJuego implements EstadoJuego {

	private final int MARGEN_X = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
	private final int MARGEN_Y = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
	private final GestorEstados ge;
	private boolean abiertasE=false,pausa=false;
	
	Mapa mapa;
	// File audio = new File("recursos/audio/temaPrincipalHope.wav");
	Jugador jugador;

	ArrayList<Enemigo> enemigos = new ArrayList<Enemigo>();
	ArrayList<Plataforma> plataformas = new ArrayList<Plataforma>();
	ArrayList<Bloque> bloques = new ArrayList<Bloque>();
	ArrayList<Objeto> corazon = new ArrayList<Objeto>();
	final ArrayList<Puerta[]> puertas=new ArrayList<Puerta[]>();

	public GestorJuego(GestorEstados ge) {
		this.ge = ge;

		// System.out.println(audio.getAbsolutePath());
		mapa = new Mapa("/mapas/nivel1");
		
		iniciarJugador();
		generadorPlataformas();
		generadorBloques();
		generadorPuertas();
		generadorEnemigos();
	


		// try {
		// Clip sonido = AudioSystem.getClip();
		// try {
		// sonido.open(AudioSystem.getAudioInputStream(audio));
		// sonido.start();
		// } catch (IOException | UnsupportedAudioFileException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } catch (LineUnavailableException e) {
		// e.printStackTrace();
		// }
	}

	public GestorJuego(GestorEstados ge, String url, Point puntoEntrada) {
		this.ge = ge;
		mapa = new Mapa(url, puntoEntrada);

		iniciarJugador();
		
		generadorPlataformas();
		generadorBloques();
		generadorPuertas();
		

		generadorEnemigos();
		
	}

	private ArrayList<Bloque> generadorBloques() {

		bloques.clear();

		for (int y = 0; y < this.mapa.obtenerAlto(); y++) {
			for (int x = 0; x < this.mapa.obtenerAncho(); x++) {
				int puntoX = x * Constantes.LADO_SPRITE  + MARGEN_X;
				int puntoY = y * Constantes.LADO_SPRITE  + MARGEN_Y;

				if (4 == mapa.obtenerEnemigos()[x + y * this.mapa.obtenerAncho()]) {

					final Point punto = new Point(puntoX, puntoY);
					final Bloque e = new Bloque(jugador, punto, mapa);

					bloques.add(e);
				}
			}
		}

		return bloques;
	}
	private ArrayList<Puerta[]> generadorPuertas() {
		
		puertas.clear();
		abiertasE=false;
		String temp = "";
		Point puntoTemp = new Point(0, 0);
		for (int y = 0; y < this.mapa.obtenerAlto(); y++) {
			for (int x = 0; x < this.mapa.obtenerAncho(); x++) {
				
				if(mapa.obtenerPuertas()[x + y * this.mapa.obtenerAncho()].length()==2){
					
					if (mapa.obtenerPuertas()[x + y * this.mapa.obtenerAncho()].equals(temp)) {
						Puerta[] puerta=new Puerta[y-(int)puntoTemp.getY()+1];
						for(int i=0;i<=y-puntoTemp.getY();i++){
							int puntoX = (int)puntoTemp.getX()* Constantes.LADO_SPRITE  + MARGEN_X;
							int puntoY =((int)puntoTemp.getY()+ i)* Constantes.LADO_SPRITE  + MARGEN_Y;
							
							Point puntoPuerta=new Point(puntoX,puntoY);
							Puerta p=new Puerta(jugador,puntoPuerta,mapa,x);
							puerta[i]=p;
						}
						puertas.add(puerta);
					}
					puntoTemp = new Point(x, y);
					temp = mapa.obtenerPuertas()[x + y * this.mapa.obtenerAncho()];
				}
			}
		}

		return puertas;
	}
	private void abrirPuertas() {
		abiertasE=true;
		for (int i = 0;i <puertas.size(); i++) {
			for(int z=0;z<puertas.get(i).length;z++){
				puertas.get(i)[z].abrirPuerta(true,puertas.get(i).length-1-z);
				
			}
		}
		
	}

	private ArrayList<Enemigo> generadorEnemigos() {

		enemigos.clear();

		for (int y = 0; y < this.mapa.obtenerAlto(); y++) {
			for (int x = 0; x < this.mapa.obtenerAncho(); x++) {
				int puntoX = x * Constantes.LADO_SPRITE + MARGEN_X;
				int puntoY = y * Constantes.LADO_SPRITE + MARGEN_Y;

				if (3 == mapa.obtenerEnemigos()[x + y * this.mapa.obtenerAncho()]) {

					final Point punto = new Point(puntoX, puntoY);
					final Enemigo e = new Enemigo(mapa, punto,puertas,3);
					enemigos.add(e);
				}else if(5 == mapa.obtenerEnemigos()[x + y * this.mapa.obtenerAncho()]){
					final Point punto = new Point(puntoX, puntoY);
					final Enemigo e = new Enemigo(mapa, punto,puertas,4);
					enemigos.add(e);
				}
			}
		}
		

		return enemigos;
	}

	private ArrayList<Plataforma> generadorPlataformas() {

		plataformas.clear();
		String temp = "";
		Point puntoTemp = new Point(0, 0);
		// cada dos puntos se creara una plataforma que se desplaza de punto a
		// punto
		for (int y = 0; y < this.mapa.obtenerAlto(); y++) {
			for (int x = 0; x < this.mapa.obtenerAncho(); x++) {
				int puntoX = x * Constantes.LADO_SPRITE  + MARGEN_X;
				int puntoY = y * Constantes.LADO_SPRITE  + MARGEN_Y;
			if (mapa.obtenerPlataformas()[x + y * this.mapa.obtenerAncho()].length()==2) {
				if (mapa.obtenerPlataformas()[x + y * this.mapa.obtenerAncho()].equals(temp)) {

					final Point punto = new Point(puntoX, puntoY);
					final Plataforma p = new Plataforma(mapa, puntoTemp, punto);

					plataformas.add(p);

				}
				puntoTemp = new Point(puntoX, puntoY);
				temp = mapa.obtenerPlataformas()[x + y * this.mapa.obtenerAncho()];
			}
				
			}
		}

		return plataformas;
	}
	public void continuar(){
		jugador.establecerSalud(1);
		recargarJuego(mapa.obtenerRuta());
		
	}
	private void recargarJuego(String ruta) {
		
		enemigos.clear();
		plataformas.clear();
		bloques.clear();
		corazon.clear();
		
		iniciarMapa(ruta);
		
		
		iniciarJugador();
		generadorPlataformas();
		generadorPuertas();
		generadorBloques();

		generadorEnemigos();


		 jugador.establecerPosicionX(mapa.obtenerPosicionInicial().x);
		 jugador.establecerPosicionY(mapa.obtenerPosicionInicial().y);
	}

	private void cargarSiguienteMapa() {
		final String ruta = "/mapas/" + mapa.obtenerSiguienteMapa();
		enemigos.clear();
		plataformas.clear();
		bloques.clear();
		corazon.clear();
		jugador.establecerPosicionX(0);
		jugador.establecerPosicionY(0);
		iniciarMapa(ruta, mapa.obtenerEntrada());
		
		jugador.establecerPosicionX(mapa.obtenerPosicionInicial().x);
		jugador.establecerPosicionY(mapa.obtenerPosicionInicial().y);
		int salud =jugador.obtenerSalud();
		iniciarJugador(salud);
		
		generadorPlataformas();
		generadorPuertas();
		generadorBloques();

		generadorEnemigos();


		// jugador.establecerPosicionX(mapa.obtenerPosicionInicial().x);
		// jugador.establecerPosicionY(mapa.obtenerPosicionInicial().y);
	}

	private void iniciarJugador() {
		jugador = new Jugador(mapa, enemigos, plataformas, bloques,puertas);
	}
	
	private void iniciarJugador(int salud) {
		jugador = new Jugador(mapa, enemigos, plataformas, bloques,puertas,salud);
	}
	

	private void iniciarMapa(final String ruta, final Point entrada) {
		mapa = new Mapa(ruta, entrada);
	}
	private void iniciarMapa(final String ruta) {
		mapa = new Mapa(ruta);
	}

	public void actualizar() {

		if(GestorControles.teclado.p.estaPulsada()){
			ge.cambiarEstadoActual(3);
		}
		
		
		if (jugador.obtenerLIMITE_DERECHA().intersects(mapa.obtenerZonaSalida())) {
			cargarSiguienteMapa();
		}
		jugador.actualizar();
		if(jugador.obtenerSalud()<=0){
			ge.cambiarEstadoActual(1);
		}
		mapa.actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
		
		if(!puertas.isEmpty()){
			for (int i = 0; i <puertas.size(); i++) {
				for(int z=0;z<puertas.get(i).length;z++){
					puertas.get(i)[z].actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
				}
			}
		}
			
		if (!enemigos.isEmpty()) {
			for (int i = 0; i < enemigos.size(); i++) {
				enemigos.get(i).actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
			}
		}else if(!abiertasE){
			abrirPuertas();
		}
		if (!bloques.isEmpty()) {
			for (int i = 0; i < bloques.size(); i++) {
				bloques.get(i).actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
			}
		}
		if (!plataformas.isEmpty()) {
			for (int i = 0; i < plataformas.size(); i++) {
				plataformas.get(i).actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
			}
		}

		if (!corazon.isEmpty()) {
			for (int i = 0; i < corazon.size(); i++) {
				corazon.get(i).actualizar((int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
				if (jugador.obtenerLIMITE_DERECHA().intersects(corazon.get(i).obtenerColision())
						|| jugador.obtenerLIMITE_IZQUIERDA().intersects(corazon.get(i).obtenerColision())) {
					corazon.remove(i);// cojer corazon
					jugador.cojerCorazon(1);
				}
			}
		}
	
		

	}

	public void dibujar(Graphics g) {
		

		mapa.dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());

		// if (enemigo.estaVivo() || enemigo.obtenerMuriendo() < 5) {
		for (int i = 0; i < enemigos.size(); i++) {
			if (enemigos.get(i).estaVivo()) {
				enemigos.get(i).dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
			} else {
				Point p = new Point(enemigos.get(i).obtenerPosicion());
				corazon.add(new Objeto(mapa, 0, p));
				enemigos.remove(i);
				i--;
			}

		}
		for (int i = 0; i <puertas.size(); i++) {
			for(int z=0;z<puertas.get(i).length;z++){
				puertas.get(i)[z].dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
			}
		}
		for (int i = 0; i < corazon.size(); i++) {
			corazon.get(i).dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());

		}

		for (int i = 0; i < plataformas.size(); i++) {
			plataformas.get(i).dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
		}

		for (int i = 0; i < bloques.size(); i++) {
			bloques.get(i).dibujar(g, (int) jugador.obtenerPosicionX(), (int) jugador.obtenerPosicionY());
		}
		
		jugador.dibujar(g);

		//
		// } else {
		// corazon.dibujar(g, (int) jugador.obtenerPosicionX(),
		// (int) jugador.obtenerPosicionY());
		//
		// }

		// g.setColor(Color.RED);
		// g.drawString("X = " + jugador.obtenerPosicionX(), 20, 20);
		// g.drawString("Y = " + jugador.obtenerPosicionY(), 20, 30);
		// g.drawString("Siguiente mapa: " + mapa.obtenerSiguienteMapa(), 20,
		// 100);
		// g.drawString("Cordenadas Salida X: " +
		// mapa.obtenerPuntoSalida().getX(), 20, 110);
		// g.drawString("Cordenadas Salida Y: " +
		// mapa.obtenerPuntoSalida().getY(), 20, 120);
		//
		// g.fillRect(mapa.obtenerZonaSalida().x, mapa.obtenerZonaSalida().y,
		// mapa.obtenerZonaSalida().width,
		// mapa.obtenerZonaSalida().height);
	}
}
