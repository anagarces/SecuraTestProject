import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminContenidoCreateComponent } from './admin-contenido-create.component';

describe('AdminContenidoCreateComponent', () => {
  let component: AdminContenidoCreateComponent;
  let fixture: ComponentFixture<AdminContenidoCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminContenidoCreateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminContenidoCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
