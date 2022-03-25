import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('SeniorityLevel e2e test', () => {
  const seniorityLevelPageUrl = '/seniority-level';
  const seniorityLevelPageUrlPattern = new RegExp('/seniority-level(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const seniorityLevelSample = { name: 'International redefine' };

  let seniorityLevel: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/seniority-levels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/seniority-levels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/seniority-levels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (seniorityLevel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/seniority-levels/${seniorityLevel.id}`,
      }).then(() => {
        seniorityLevel = undefined;
      });
    }
  });

  it('SeniorityLevels menu should load SeniorityLevels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('seniority-level');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SeniorityLevel').should('exist');
    cy.url().should('match', seniorityLevelPageUrlPattern);
  });

  describe('SeniorityLevel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(seniorityLevelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SeniorityLevel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/seniority-level/new$'));
        cy.getEntityCreateUpdateHeading('SeniorityLevel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityLevelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/seniority-levels',
          body: seniorityLevelSample,
        }).then(({ body }) => {
          seniorityLevel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/seniority-levels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [seniorityLevel],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(seniorityLevelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SeniorityLevel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('seniorityLevel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityLevelPageUrlPattern);
      });

      it('edit button click should load edit SeniorityLevel page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SeniorityLevel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityLevelPageUrlPattern);
      });

      it('last delete button click should delete instance of SeniorityLevel', () => {
        cy.intercept('GET', '/api/seniority-levels/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('seniorityLevel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', seniorityLevelPageUrlPattern);

        seniorityLevel = undefined;
      });
    });
  });

  describe('new SeniorityLevel page', () => {
    beforeEach(() => {
      cy.visit(`${seniorityLevelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SeniorityLevel');
    });

    it('should create an instance of SeniorityLevel', () => {
      cy.get(`[data-cy="name"]`).type('Inverso multimedia Acero').should('have.value', 'Inverso multimedia Acero');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        seniorityLevel = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', seniorityLevelPageUrlPattern);
    });
  });
});
