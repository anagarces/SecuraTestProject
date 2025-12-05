export interface SubmissionSummary {
  id: number;
  quizId: number | null;
  quizTitle: string | null;
  userEmail: string;
  userName: string;
  score: number;
  totalQuestions: number;
  submittedAt: string; 
  finishedAt: string;  
}
