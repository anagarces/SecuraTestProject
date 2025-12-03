export interface User {
  id: number;
  nombre: string;
  email: string;
  role: string;   // 'USER' | 'ADMIN'
  active: boolean;
}
