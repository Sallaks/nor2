import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SeniorityLevel from './seniority-level';
import SeniorityLevelDetail from './seniority-level-detail';
import SeniorityLevelUpdate from './seniority-level-update';
import SeniorityLevelDeleteDialog from './seniority-level-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SeniorityLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SeniorityLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SeniorityLevelDetail} />
      <ErrorBoundaryRoute path={match.url} component={SeniorityLevel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SeniorityLevelDeleteDialog} />
  </>
);

export default Routes;
