import { OptionItem } from './option-item'; // cada pregunta tiene opciones

// Representa una pregunta dentro de un cuestionario
export interface Question {
   id: number;            // Identificador de la pregunta
   text: string;          // Texto de la pregunta
   options: OptionItem[]; // Posibles respuestas (opciones)
}