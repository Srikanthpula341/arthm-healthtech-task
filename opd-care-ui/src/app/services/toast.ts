import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
  id: number;
  type: 'SUCCESS' | 'ERROR';
  title: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  public toasts = signal<ToastMessage[]>([]);
  private idCounter = 0;

  showSuccess(title: string, message: string) {
    this.addToast('SUCCESS', title, message);
  }

  showError(title: string, message: string) {
    this.addToast('ERROR', title, message);
  }

  private addToast(type: 'SUCCESS' | 'ERROR', title: string, message: string) {
    const id = ++this.idCounter;
    this.toasts.update(current => [...current, { id, type, title, message }]);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
      this.remove(id);
    }, 5000);
  }

  remove(id: number) {
    this.toasts.update(current => current.filter(t => t.id !== id));
  }
}
