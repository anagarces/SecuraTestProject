export interface SubmissionSummary {
  id: number;
  quizId: number | null;
  quizTitle: string | null;
  score: number;
  totalQuestions: number;
  submittedAt: string; // ISO string desde el backend
  finishedAt: string;  // ISO string desde el backend
}
