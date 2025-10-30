// Representa una respuesta que el usuario selecciona
export interface SubmissionAnswer {
  
  questionId: number; // ID de la pregunta respondida
  optionId: number;   // ID de la opción elegida por el usuario
}