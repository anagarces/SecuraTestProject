import { Component, OnInit } from '@angular/core';
import { IContenido } from '../model/contenido';
import { Observable } from 'rxjs';
import { ContentService } from '../services/content.service';


@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent  implements OnInit{


  public todosLosContenidos: IContenido[] = []; //lista completa de contenidos
  public cardsAMostrar: IContenido[] = []; //almacena las cards de la pagina actual
  public isLoading: boolean = true; //para mostrar el mensaje 'cargando...'


  //Configuracion de la paginacion
  public paginaActual: number = 1;
  public cardsPorPagina: number = 6; 


//modal bienvenida
showWelcomeLogin = false;
welcomeUserName = '';
animatePage = false;


  constructor(private contentService: ContentService){}

 ngOnInit(): void {

  // ========= Modal si viene del LOGIN =========
  if(localStorage.getItem('welcome_login')){

    this.welcomeUserName = localStorage.getItem('welcome_name') || 'Usuario';
    this.showWelcomeLogin = true;

    localStorage.removeItem('welcome_login');
  }

  // ========= Efecto Fade-In al cargar contenido =========
  setTimeout(() => this.animatePage = true, 100);

  // ========= Cargar Contenido =========
  this.contentService.getContenidos().subscribe(contenidosRecibidos=>{

    this.todosLosContenidos = contenidosRecibidos;
    this.actualizarCardsVisibles();
    this.isLoading = false;

  });
}

closeWelcomeLogin(){
  this.showWelcomeLogin = false;
}

    

     // --- Métodos para la paginación---

  actualizarCardsVisibles(): void {
    const indiceInicial = (this.paginaActual - 1) * this.cardsPorPagina;
    const indiceFinal = indiceInicial + this.cardsPorPagina;
    this.cardsAMostrar = this.todosLosContenidos.slice(indiceInicial, indiceFinal);
  }

  irAPaginaSiguiente(): void {
    if (this.paginaActual < this.totalPaginas()) {
      this.paginaActual++;
      this.actualizarCardsVisibles();
    }
  }

  irAPaginaAnterior(): void {
    if (this.paginaActual > 1) {
      this.paginaActual--;
      this.actualizarCardsVisibles();
    }
  }

  totalPaginas(): number {
    return Math.ceil(this.todosLosContenidos.length / this.cardsPorPagina);
  }
}
  


