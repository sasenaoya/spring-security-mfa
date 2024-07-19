import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { mergeMap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MfaService {
  constructor(
    private http: HttpClient,
    private accountService: AccountService,
  ) {}

  postMfa(code: string) {
    return this.http.post('api/mfa', { code }).pipe(mergeMap(() => this.accountService.identity(true)));
  }
}
