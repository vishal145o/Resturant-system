import { useEffect, useMemo, useState } from "react";

const API_BASE = "http://localhost:8080";

export default function App() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // customer (cart is per customer name)
  const [customer, setCustomer] = useState("");
  const [cart, setCart] = useState({ customerName: "", items: [], totalAmount: 0 });
  const [cartLoading, setCartLoading] = useState(false);

  // menu form
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [editId, setEditId] = useState(null);
  const [available, setAvailable] = useState(true);

  const totalItems = useMemo(() => items.length, [items]);
  const availableCount = useMemo(() => items.filter(i => i.available).length, [items]);

  async function loadMenu() {
    setLoading(true);
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/menu`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      setItems(data);
    } catch (e) {
      setError("Backend not reachable. Start Spring Boot on :8080");
    } finally {
      setLoading(false);
    }
  }

  async function loadCart(customerName) {
    if (!customerName?.trim()) {
      setCart({ customerName: "", items: [], totalAmount: 0 });
      return;
    }
    setCartLoading(true);
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/cart?customer=${encodeURIComponent(customerName)}`);
      const data = await res.json();
      if (!res.ok) {
        setError(data?.message || "Failed to load cart");
        return;
      }
      setCart(data);
    } catch {
      setError("Network error while loading cart");
    } finally {
      setCartLoading(false);
    }
  }

  useEffect(() => { loadMenu(); }, []);

  function resetForm() {
    setName("");
    setPrice("");
    setEditId(null);
    setAvailable(true);
  }

  async function onSubmit(e) {
    e.preventDefault();
    setError("");

    const payload = {
      name: name.trim(),
      price: Number(price),
      ...(editId !== null ? { available } : {})
    };

    try {
      const res = await fetch(`${API_BASE}/api/menu${editId !== null ? `/${editId}` : ""}`, {
        method: editId !== null ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      const isNoContent = res.status === 204;
      const body = !isNoContent ? await res.json().catch(() => null) : null;

      if (!res.ok) {
        setError(body?.message || "Request failed");
        return;
      }

      await loadMenu();
      resetForm();
    } catch {
      setError("Network error");
    }
  }

  async function onDelete(id) {
    if (!confirm("Delete this item?")) return;
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/menu/${id}`, { method: "DELETE" });
      if (!res.ok && res.status !== 204) {
        const body = await res.json().catch(() => null);
        setError(body?.message || "Delete failed");
        return;
      }
      await loadMenu();
    } catch {
      setError("Network error");
    }
  }

  function startEdit(item) {
    setEditId(item.id);
    setName(item.name);
    setPrice(String(item.price));
    setAvailable(!!item.available);
  }

  async function addToCart(menuItemId) {
    if (!customer.trim()) {
      setError("Please enter customer name first (for cart).");
      return;
    }
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/cart/items?customer=${encodeURIComponent(customer)}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ menuItemId, quantity: 1 })
      });
      const data = await res.json().catch(() => null);
      if (!res.ok) {
        setError(data?.message || "Failed to add to cart");
        return;
      }
      setCart(data);
    } catch {
      setError("Network error");
    }
  }

  async function changeQty(menuItemId, quantity) {
    if (!customer.trim()) return;
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/cart/items/${menuItemId}?customer=${encodeURIComponent(customer)}&quantity=${quantity}`, {
        method: "PUT"
      });
      const data = await res.json().catch(() => null);
      if (!res.ok) {
        setError(data?.message || "Failed to update quantity");
        return;
      }
      setCart(data);
    } catch {
      setError("Network error");
    }
  }

  async function clearCart() {
    if (!customer.trim()) return;
    setError("");
    try {
      const res = await fetch(`${API_BASE}/api/cart?customer=${encodeURIComponent(customer)}`, { method: "DELETE" });
      const data = await res.json().catch(() => null);
      if (!res.ok) {
        setError(data?.message || "Failed to clear cart");
        return;
      }
      setCart(data);
    } catch {
      setError("Network error");
    }
  }

  const cartTotalItems = useMemo(() => cart.items?.reduce((s, i) => s + i.quantity, 0) || 0, [cart]);

  return (
    <div className="min-h-screen">
      <header className="border-b bg-white">
        <div className="mx-auto max-w-5xl px-4 py-4 flex items-center justify-between">
          <div>
            <h1 className="text-xl font-semibold">Restaurant Basic (Student Project)</h1>
            <p className="text-sm text-slate-500">React + Tailwind + Spring Boot (in-memory)</p>
          </div>
          <div className="flex gap-2">
            <button
              onClick={loadMenu}
              className="rounded-lg border px-3 py-2 text-sm hover:bg-slate-50"
            >
              Refresh
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-6 space-y-6">
        <div className="rounded-xl bg-white border p-4">
          <h2 className="font-semibold mb-3">Customer (for Add to Cart)</h2>
          <div className="flex flex-col sm:flex-row gap-3 sm:items-end">
            <label className="block flex-1">
              <span className="text-sm text-slate-600">Customer Name</span>
              <input
                value={customer}
                onChange={(e) => setCustomer(e.target.value)}
                className="mt-1 w-full rounded-lg border px-3 py-2"
                placeholder="e.g., Vishal"
              />
            </label>
            <button
              onClick={() => loadCart(customer)}
              className="rounded-lg bg-black text-white px-4 py-2 text-sm hover:opacity-90"
              disabled={cartLoading}
            >
              Load Cart
            </button>
            <button
              onClick={clearCart}
              className="rounded-lg border px-4 py-2 text-sm hover:bg-slate-50"
              disabled={!customer.trim()}
            >
              Clear Cart
            </button>
          </div>
          <p className="mt-2 text-xs text-slate-500">
            Cart is stored customer-wise in backend memory (for learning).
          </p>
        </div>

        {error && (
          <div className="rounded-lg border border-red-200 bg-red-50 p-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="rounded-xl bg-white border p-4">
            <p className="text-sm text-slate-500">Total Menu Items</p>
            <p className="text-2xl font-semibold">{totalItems}</p>
          </div>
          <div className="rounded-xl bg-white border p-4">
            <p className="text-sm text-slate-500">Available</p>
            <p className="text-2xl font-semibold">{availableCount}</p>
          </div>
          <div className="rounded-xl bg-white border p-4">
            <p className="text-sm text-slate-500">Cart Items</p>
            <p className="text-2xl font-semibold">{cartTotalItems}</p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Menu form */}
          <div className="rounded-xl bg-white border p-4">
            <h2 className="font-semibold mb-3">{editId !== null ? "Edit Menu Item" : "Add Menu Item"}</h2>

            <form onSubmit={onSubmit} className="grid grid-cols-1 sm:grid-cols-4 gap-3 items-end">
              <label className="block sm:col-span-2">
                <span className="text-sm text-slate-600">Name</span>
                <input
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="mt-1 w-full rounded-lg border px-3 py-2"
                  placeholder="e.g., Sandwich"
                  required
                />
              </label>

              <label className="block">
                <span className="text-sm text-slate-600">Price</span>
                <input
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  className="mt-1 w-full rounded-lg border px-3 py-2"
                  type="number"
                  min="1"
                  placeholder="e.g., 120"
                  required
                />
              </label>

              {editId !== null && (
                <label className="flex items-center gap-2 sm:pt-7">
                  <input
                    type="checkbox"
                    checked={available}
                    onChange={(e) => setAvailable(e.target.checked)}
                  />
                  <span className="text-sm text-slate-700">Available</span>
                </label>
              )}

              <div className="flex gap-2 sm:col-span-4">
                <button
                  type="submit"
                  className="rounded-lg bg-black text-white px-4 py-2 text-sm hover:opacity-90"
                  disabled={loading}
                >
                  {editId !== null ? "Update" : "Add"}
                </button>

                {editId !== null && (
                  <button
                    type="button"
                    onClick={resetForm}
                    className="rounded-lg border px-4 py-2 text-sm hover:bg-slate-50"
                  >
                    Cancel
                  </button>
                )}
              </div>
            </form>
          </div>

          {/* Cart panel */}
          <div className="rounded-xl bg-white border">
            <div className="p-4 border-b flex items-center justify-between">
              <div>
                <h2 className="font-semibold">Cart</h2>
                <p className="text-xs text-slate-500">
                  {customer.trim() ? `Customer: ${customer}` : "Enter customer name to use cart"}
                </p>
              </div>
              <div className="text-sm font-medium">Total: ₹ {cart.totalAmount || 0}</div>
            </div>

            <div className="divide-y">
              {(cart.items || []).map((ci) => (
                <div key={ci.menuItemId} className="p-4 flex items-center justify-between gap-3">
                  <div>
                    <p className="font-medium">{ci.menuItemName}</p>
                    <p className="text-sm text-slate-500">₹ {ci.unitPrice} × {ci.quantity} = ₹ {ci.lineTotal}</p>
                  </div>
                  <div className="flex items-center gap-2">
                    <button
                      className="rounded-lg border px-3 py-2 text-sm hover:bg-slate-50"
                      onClick={() => changeQty(ci.menuItemId, ci.quantity - 1)}
                      disabled={!customer.trim()}
                    >
                      -
                    </button>
                    <span className="min-w-6 text-center text-sm">{ci.quantity}</span>
                    <button
                      className="rounded-lg border px-3 py-2 text-sm hover:bg-slate-50"
                      onClick={() => changeQty(ci.menuItemId, ci.quantity + 1)}
                      disabled={!customer.trim()}
                    >
                      +
                    </button>
                  </div>
                </div>
              ))}

              {(cart.items || []).length === 0 && (
                <div className="p-6 text-sm text-slate-500">
                  Cart is empty. Click “Add to Cart” from menu items.
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Menu list */}
        <div className="rounded-xl bg-white border">
          <div className="p-4 border-b flex items-center justify-between">
            <h2 className="font-semibold">Menu Items</h2>
            {loading && <span className="text-sm text-slate-500">Loading...</span>}
          </div>

          <div className="divide-y">
            {items.map((item) => (
              <div key={item.id} className="p-4 flex items-center justify-between gap-3">
                <div>
                  <p className="font-medium">{item.name}</p>
                  <p className="text-sm text-slate-500">₹ {item.price} · {item.available ? "Available" : "Out of stock"}</p>
                </div>

                <div className="flex gap-2 flex-wrap justify-end">
                  <button
                    onClick={() => addToCart(item.id)}
                    className="rounded-lg bg-black text-white px-3 py-2 text-sm hover:opacity-90"
                    disabled={!customer.trim()}
                    title={!customer.trim() ? "Enter customer name first" : "Add to cart"}
                  >
                    Add to Cart
                  </button>
                  <button
                    onClick={() => startEdit(item)}
                    className="rounded-lg border px-3 py-2 text-sm hover:bg-slate-50"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => onDelete(item.id)}
                    className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 hover:bg-red-100"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}

            {items.length === 0 && !loading && (
              <div className="p-6 text-sm text-slate-500">No items yet. Add your first menu item above.</div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
