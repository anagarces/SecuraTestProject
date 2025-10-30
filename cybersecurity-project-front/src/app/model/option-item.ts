// Representa una opción de respuesta de una pregunta
export interface OptionItem {

   id: number;        // Identificador de la opción
  text: string;      // Texto visible para el usuario
  correct: boolean;  // Si es la respuesta correcta (solo el backend lo sabe)
  
}