import { Routes } from '@angular/router';
import { LoginComponent } from './presentation/pages/login/login.component';
import { RegisterComponent } from './presentation/pages/register/register.component';
import { VerificarCodigoComponent } from './presentation/pages/verificar-codigo/verificar-codigo.component';
import { ForgotPasswordComponent } from './presentation/pages/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './presentation/pages/reset-password/reset-password.component';

export const AUTH_ROUTES: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verificar-codigo', component: VerificarCodigoComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];
