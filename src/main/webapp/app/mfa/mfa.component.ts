import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MfaService } from './mfa.service';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-mfa',
  standalone: true,
  imports: [ReactiveFormsModule, SharedModule],
  templateUrl: './mfa.component.html',
  styleUrl: './mfa.component.scss',
})
export class MfaComponent {
  formGroup = this.fb.group({
    code: ['', Validators.required],
  });
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private mfaService: MfaService,
  ) {}
  submit(): void {
    this.mfaService.postMfa(this.formGroup.value.code!).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: e => {
        this.errorMessage = e.error.message;
      },
    });
  }
}
