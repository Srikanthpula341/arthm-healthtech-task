import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-toast',
  imports: [CommonModule],
  templateUrl: './toast.html',
  styles: [`
    .toast-container { z-index: 10000; }
    .toast { border-radius: 12px; box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15); }
    .bg-success-subtle { background-color: #e6f4ea !important; color: #1e4620 !important; border-left: 5px solid #2e7d32; }
    .bg-danger-subtle { background-color: #fdeaea !important; color: #611a15 !important; border-left: 5px solid #c62828; }
    .icon-success { color: #2e7d32; font-size: 1.5rem; }
    .icon-danger { color: #c62828; font-size: 1.5rem; }
  `]
})
export class Toast {
  public toastService = inject(ToastService);
}
