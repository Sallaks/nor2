import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './developer.reducer';
import { IDeveloper } from 'app/shared/model/developer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Developer = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const developerList = useAppSelector(state => state.developer.entities);
  const loading = useAppSelector(state => state.developer.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="developer-heading" data-cy="DeveloperHeading">
        <Translate contentKey="norApp.developer.home.title">Developers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="norApp.developer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="norApp.developer.home.createLabel">Create new Developer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {developerList && developerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="norApp.developer.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="norApp.developer.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="norApp.developer.age">Age</Translate>
                </th>
                <th>
                  <Translate contentKey="norApp.developer.skill">Skill</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {developerList.map((developer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${developer.id}`} color="link" size="sm">
                      {developer.id}
                    </Button>
                  </td>
                  <td>{developer.name}</td>
                  <td>{developer.age}</td>
                  <td>
                    {developer.skills
                      ? developer.skills.map((val, j) => (
                          <span key={j}>
                            <Link to={`skill/${val.id}`}>{val.name}</Link>
                            {j === developer.skills.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${developer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${developer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${developer.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="norApp.developer.home.notFound">No Developers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Developer;
