import { SubmissionAnswer } from './submission-answer';

// Datos que enviamos al backend cuando el usuario envía un cuestionario
export interface SubmissionRequest {
  quizId: number;              // ID del cuestionario
  answers: SubmissionAnswer[]; // Lista de respuestas del usuario
}

// Datos que el backend nos devuelve después de guardar el envío
export interface SubmissionResponse {
  quizId?: number;          // ID del cuestionario respondido
  score: number;           // Puntuación obtenida
  totalQuestions: number;  // Total de preguntas en el quiz
} 