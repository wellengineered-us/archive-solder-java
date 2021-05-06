/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

public abstract class AbstractLifecycle<TCreationException extends Exception, TDisposalException extends Exception> implements Creatable, Disposable
{
	protected AbstractLifecycle()
	{
	}

	private boolean created;
	private boolean disposed;

	@Override
	public final boolean isCreated()
	{
		return this.created;
	}

	private void setCreated(boolean created)
	{
		this.created = created;
	}

	@Override
	public final boolean isDisposed()
	{
		return this.disposed;
	}

	private void setDisposed(boolean disposed)
	{
		this.disposed = disposed;
	}

	@Override
	public final void /* dispose */ close() throws TDisposalException
	{
		this.dispose();
	}

	@Override
	public final void create() throws TCreationException
	{
		if (this.isCreated())
			return;

		//GC.ReRegisterForFinalize(this);
		this.create(true);
		this.maybeSetCreated();
	}

	protected abstract void create(boolean creating) throws TCreationException;

	@Override
	public final void dispose() throws TDisposalException
	{
		if (this.isDisposed())
			return;

		this.dispose(true);
		//GC.SuppressFinalize(this);
		this.maybeSetDisposed();
	}

	protected abstract void dispose(boolean disposing) throws TDisposalException;

	protected void explicitSetCreated()
	{
		//GC.ReRegisterForFinalize(this);
		this.setCreated(true);
	}

	protected void explicitSetDisposed()
	{
		this.setDisposed(true);
		//GC.SuppressFinalize(this);
	}

	protected void maybeSetCreated()
	{
		this.explicitSetCreated();
	}

	protected void maybeSetDisposed()
	{
		this.explicitSetDisposed();
	}
}
