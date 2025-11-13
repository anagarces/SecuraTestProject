import { Question } from './question'; //importamos interface de pregunta


//Esta interfaz representa un cuestionario completo que viene del backend
export interface Quiz {

  id?: number; // Identificador único del cuestionario
  title: string; // Título del cuestionario
  description: string; // Breve descripción (mostrada al usuario)
  questions: Question[]; // Lista de preguntas que contiene

}