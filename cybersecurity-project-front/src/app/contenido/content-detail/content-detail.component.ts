import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Location } from '@angular/common';
import { IContenido } from 'src/app/model/contenido';
import { ContentService } from 'src/app/services/content.service';



@Component({
  selector: 'app-content-detail',
  templateUrl: './content-detail.component.html',
  styleUrls: ['./content-detail.component.css']
})
export class ContentDetailComponent  implements OnInit{

  public contenido$!: Observable<IContenido | null>;
  public errorOcurrido = false;

  constructor(
     private route: ActivatedRoute, // para acceder a la info de la ruta
    private contentService: ContentService, // Inyectamos servicio
    private location: Location // Inyectamos Location para poder navegar hacia atrás
  ){}

  ngOnInit(): void {
    //Obtenemos el ID de los parámetros de la URL
    const idParam = this.route.snapshot.paramMap.get('id');

    //Nos aseguramos de que el ID no sea nulo
    if (idParam) {
      // El '+' convierte el string 'id' en un número
      const idNumerico = +idParam;

      // 3. Llamamos al servicio para obtener los datos
      this.contenido$ = this.contentService.getContenidoById(idNumerico).pipe(
        catchError(err => {
          console.error('Error al obtener el contenido:', err);
          this.errorOcurrido = true; // Activamos una bandera de error
          return of(null); // Devolvemos un observable nulo para que la app no se rompa
        })
      );
    }
  }

  // Método para el botón "Volver"
  volver(): void {
    this.location.back();
  }
}

