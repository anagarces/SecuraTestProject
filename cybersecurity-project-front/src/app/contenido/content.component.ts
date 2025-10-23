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

  constructor(private contentService: ContentService){}

  ngOnInit(): void {
      
    //obtenemos los datos una sola vez
    this.contentService.getContenidos().subscribe(contenidosRecibidos=>{

      //guarda lista completa
      this.todosLosContenidos = contenidosRecibidos;

       console.log('Datos del primer contenido:', this.todosLosContenidos[0]);

      //calculamos las cards que se deben mostrar en la primera pagina
      this.actualizarCardsVisibles();

      //Dejamos de mostrar el mensaje 'cargando'
      this.isLoading = false;

    });

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
  


