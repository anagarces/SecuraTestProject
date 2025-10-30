import { SubmissionAnswer } from './submission-answer';

// Datos que enviamos al backend cuando el usuario envía un cuestionario
export interface SubmissionRequest {
  userId: number;              // ID del usuario que responde
  quizId: number;              // ID del cuestionario
  answers: SubmissionAnswer[]; // Lista de respuestas del usuario
}

// Datos que el backend nos devuelve después de guardar el envío
export interface SubmissionResponse {
  id: number;              // ID del envío (submission)
  quizTitle: string;       // Título del cuestionario respondido
  score: number;           // Puntuación obtenida
  totalQuestions: number;  // Total de preguntas en el quiz
  submittedAt: string;     // Fecha/hora del envío
}